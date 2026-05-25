# 초특가 숙소 선착순 예약 시스템 설계 결정사항

## 📋 프로젝트 개요

- **목표**: 00시 오픈 초특가 숙소 상품(10개 한정) 선착순 예약 시스템
- **핵심 요구사항**:
  - 모든 사용자에게 동등한 기회 제공
  - 엄격한 재고 정합성 유지 (미달/초과판매 방지)
  - 결제 실패 및 시스템 장애 상황에서도 안정적 동작
  - 멱등성 처리 보장

## 🏗️ 아키텍처 설계 결정

### 1. 전체 플로우

```
00:00 요청 도착 → 멱등성 키 확인 → Redis Lua Script로 재고 선점 
→ DB에 예약 생성 → 결제 요청 → 결제 성공: 예약 확정 / 결제 실패: 재고 복구
```

### 2. 핵심 기술 스택 선택

#### 2.1 Redis + Lua Script (재고 관리)
**선택 이유:**
- **원자성**: `재고확인 + 차감 + 사용자예약 + 멱등성체크`를 하나의 원자적 연산으로 처리
- **성능**: 메모리 기반으로 00시 트래픽 폭증 대응
- **정합성**: Race Condition 완전 방지

```lua
-- 핵심 Lua Script 로직
if redis.call('EXISTS', idempotencyKey) == 1 then
    return 2  -- DUPLICATED
end

if redis.call('EXISTS', userKey) == 1 then
    return 3  -- ALREADY_RESERVED  
end

local stock = redis.call('GET', stockKey)
if tonumber(stock) <= 0 then
    return 0  -- SOLD_OUT
end

-- 원자적으로 처리
redis.call('DECR', stockKey)
redis.call('SETEX', userKey, ttlSeconds, '1')
redis.call('SETEX', idempotencyKey, ttlSeconds, '1')
return 1  -- SUCCESS
```

#### 2.2 기존 Booking 도메인 확장 (vs 새 도메인 생성)
**선택한 방식: 기존 Booking 확장**

**이유:**
- **재사용성**: 기존 `Money`, `idempotencyKey` 구조 활용
- **일관성**: 통합된 예약 관리 체계
- **점진적 도입**: 기존 시스템과 자연스러운 통합

**구체적 확장:**
```java
public enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED, COMPLETED, FAILED,
    FLASH_RESERVED,    // 초특가 재고 선점 완료
    FLASH_EXPIRED      // 초특가 예약 만료
}

public class Booking {
    private ZonedDateTime flashSaleExpiresAt; // 추가 필드
    
    // 추가 메서드들
    public void confirmFlashSale() { ... }
    public void expireFlashSale() { ... }
    public boolean isFlashSaleExpired() { ... }
}
```

## 🛡️ 정합성 보장 전략

### 3. 3단계 검증 체계

#### 3.1 1차: Redis Lua Script (실시간)
- 동시성 제어 및 빠른 응답
- 재고 차감, 중복 방지, 사용자 예약 상태 원자적 처리

#### 3.2 2차: Database Unique Constraint (영구)
- `idempotency_key` UNIQUE 제약
- `(product_id, user_id, status)` 조합 검증
- Redis 장애 시에도 중복 방지 보장

#### 3.3 3차: 스케줄러 보정 (복구)
- 30초마다 만료된 예약 자동 처리
- Redis-DB 간 불일치 상황 복구
- 실패한 보상 트랜잭션 재시도

### 4. 멱등성 처리 설계

**클라이언트 제공 idempotencyKey 활용**
```java
// 처리 순서
1. DB에서 idempotencyKey 존재 확인 (영구 보장)
2. 존재하면 기존 결과 반환
3. 없으면 Redis Lua Script 실행
4. 성공 시 DB 저장 (idempotencyKey unique)
```

**장점:**
- 서버 재시작, Redis 장애와 무관하게 멱등성 보장
- 클라이언트 재시도 시 안전한 결과 반환

## 🚨 장애 대응 전략

### 5. Redis 장애 시 대응

**선택한 전략: Fail-Closed (추천)**

```java
@CircuitBreaker(name = "redis", fallbackMethod = "failClosed")
public ReservationResult reserve(ReservationRequest request) {
    return redisBasedReservation(request);
}

public ReservationResult failClosed(ReservationRequest request, Exception ex) {
    log.error("Redis failure - closing reservations temporarily", ex);
    return ReservationResult.temporarilyUnavailable();
}
```

**근거:**
1. **정합성 우선**: 초특가 10개 한정에서는 초과판매 방지가 최우선
2. **인프라 제약**: 증설 제한 상황에서 DB 부하 급증 위험
3. **사용자 경험**: "잠시 후 재시도" 안내가 초과판매보다 선호됨

**대안 (DB Fallback) 고려사항:**
- 장점: Redis 장애 시에도 예약 가능
- 단점: 00시 트래픽이 DB 직접 몰림, Connection Pool 고갈 위험
- 결론: 제한적 상황에서만 고려 (Rate Limiting + Conditional Update)

### 6. 결제 실패 처리

#### 6.1 실시간 보상 처리
```java
public void fail(String idempotencyKey) {
    // 1. DB 상태 변경
    booking.failFlashSale();
    bookingRepository.save(booking);
    
    // 2. Redis 재고 복구
    flashSaleStockGate.release(productId, userId, idempotencyKey);
}
```

#### 6.2 스케줄러 보정 (Fallback)
```java
@Scheduled(fixedDelay = 30_000)
public void expireReservations() {
    // FLASH_RESERVED 상태이면서 만료 시간 지난 예약들 조회
    List<Booking> expired = bookingRepository.findExpiredFlashSaleReservations(now);
    
    for (Booking booking : expired) {
        booking.expireFlashSale();  // FLASH_EXPIRED로 상태 변경
        flashSaleStockGate.release(...);  // Redis 재고 복구
    }
}
```

**이중 보장 체계:**
- 실시간 처리 실패 시 스케줄러가 30초 내 복구
- Redis 복구 실패해도 계속 재시도 (로그로 추적)

## ⚡ 성능 최적화

### 7. 응답 시간 최적화
- **Redis TTL**: 5분 결제 타임아웃으로 메모리 효율성
- **Connection Pool**: Redis 전용 풀 사전 warming
- **Lua Script**: 네트워크 라운드트립 최소화

### 8. 공정성 보장
**정의**: "서버가 00:00 이후 정상 수신한 요청을 동일한 규칙으로 원자적 처리"

- 클라이언트 시간 차이 불가피 (네트워크, 지역)
- 서버 수신 시간 기준 + Redis 원자적 처리로 공정성 확보
- 완벽한 공정성보다 **예측 가능한 공정성** 제공

## 📊 모니터링 지표

### 9. 핵심 메트릭
- **재고 현황**: 실시간 Redis 재고 상태
- **예약 성공률**: 성공/실패/중복/만료 비율  
- **응답 시간**: 95% percentile 목표 < 100ms
- **Redis 가용성**: Circuit Breaker 상태
- **스케줄러 복구**: 만료 처리 건수/시간

### 10. 알림 설정
- Redis 장애 즉시 알림
- 예약 성공률 < 90% 시 알림
- 응답 시간 > 500ms 지속 시 알림

## 🔧 운영 고려사항

### 11. 배포 전 체크리스트
1. **Redis 재고 초기화**: `flashSaleStockGate.initializeStock(productId, 10)`
2. **DB 인덱스 확인**: `flash_sale_expires_at`, `idempotency_key`
3. **Connection Pool 튜닝**: Redis, DB 각각
4. **스케줄러 활성화**: `@EnableScheduling`

### 12. 장애 복구 절차
1. **Redis 장애**: Circuit Breaker로 자동 Fail-Closed
2. **DB 장애**: 기존 예약 조회 불가, 신규 예약 중단
3. **어플리케이션 재시작**: 스케줄러가 일관성 자동 복구

---

## 📝 최종 결론

본 설계는 **안정성과 정합성을 최우선**으로 하여:

1. **Redis Lua Script**로 원자성 보장
2. **Fail-Closed 전략**으로 초과판매 방지  
3. **3단계 검증**으로 완벽한 정합성
4. **스케줄러 보정**으로 장애 상황 대응

초특가 10개 한정 상품의 특성상 **"못 사는 것"보다 "초과판매"가 더 큰 리스크**라는 판단 하에, 보수적이지만 안전한 접근을 채택했습니다.