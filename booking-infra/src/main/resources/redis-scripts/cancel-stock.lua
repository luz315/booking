-- 재고 해제 Lua Script
-- KEYS[1] = userKey
-- KEYS[2] = idempotencyRedisKey  
-- KEYS[3..n] = stockKeys
-- ARGV[1] = requestIdempotencyKey

local userKey = KEYS[1]
local idempotencyRedisKey = KEYS[2]
local requestIdempotencyKey = ARGV[1]

-- 현재 선점한 idempotencyKey 조회
local currentIdempotencyKey = redis.call('GET', userKey)

if not currentIdempotencyKey then
    return 'NOT_HELD'
end

if currentIdempotencyKey ~= requestIdempotencyKey then
    return 'NOT_OWNER'
end

-- 모든 날짜 재고 복구
for i = 3, #KEYS do
    redis.call('INCR', KEYS[i])
end

-- 예약 키 삭제
redis.call('DEL', userKey)
redis.call('DEL', idempotencyRedisKey)

-- TTL 복구용 holdInfo 키도 삭제
local holdInfoKey = 'flash-sale:hold:' .. requestIdempotencyKey
redis.call('DEL', holdInfoKey)

return 'RELEASED'