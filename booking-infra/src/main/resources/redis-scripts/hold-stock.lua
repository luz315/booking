-- 재고 선점 Lua Script
-- KEYS[1] = userKey (예: "flash-sale:{123}:user:456")
-- KEYS[2] = idempotencyKey (예: "flash-sale:{123}:idempotency:abc")
-- KEYS[3..n] = stockKeys (예: "flash-sale:{123}:stock:2026-05-16")
-- ARGV[1] = ttlSeconds (예: 300)
-- ARGV[2] = requestIdempotencyKey (예: "abc")

local userKey = KEYS[1]
local idempotencyKey = KEYS[2] 
local ttlSeconds = tonumber(ARGV[1])
local requestIdempotencyKey = ARGV[2]

-- 멱등성 키 중복 확인
if redis.call('EXISTS', idempotencyKey) == 1 then
    return 'DUPLICATED'
end

-- 사용자 중복 예약 확인
if redis.call('EXISTS', userKey) == 1 then
    return 'ALREADY_HELD'
end

-- 모든 날짜의 재고 확인
for i = 3, #KEYS do
    local stockKey = KEYS[i]
    local stock = tonumber(redis.call('GET', stockKey) or '0')
    if stock <= 0 then
        -- stockKey에서 날짜 추출하여 반환
        local date = string.match(stockKey, ':(%d%d%d%d%-%d%d%-%d%d)$')
        return 'SOLD_OUT:' .. (date or 'unknown')
    end
end

-- 모든 날짜 재고 차감 (원자적)
for i = 3, #KEYS do
    redis.call('DECR', KEYS[i])
end

-- 예약 상태 저장 (userKey에 raw idempotencyKey 저장)
redis.call('SETEX', userKey, ttlSeconds, requestIdempotencyKey)
redis.call('SETEX', idempotencyKey, ttlSeconds, 'HELD')

-- TTL 만료 시 자동 복구를 위한 선점 정보 저장
local holdInfoKey = 'flash-sale:hold:' .. requestIdempotencyKey
local stockKeysJson = '['
for i = 3, #KEYS do
    if i > 3 then
        stockKeysJson = stockKeysJson .. ','
    end
    stockKeysJson = stockKeysJson .. '"' .. KEYS[i] .. '"'
end
stockKeysJson = stockKeysJson .. ']'

redis.call('SETEX', holdInfoKey, ttlSeconds + 60, stockKeysJson)

return 'SUCCESS'