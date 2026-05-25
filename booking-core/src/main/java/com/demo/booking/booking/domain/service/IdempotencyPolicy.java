package com.demo.booking.booking.domain.service;

import java.time.Duration;
import java.util.UUID;

public class IdempotencyPolicy {
    
    /**
     * 멱등성 키의 유효성을 검증합니다.
     */
    public boolean isValidIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) {
            return false;
        }
        
        // 길이 제한: 1-255자
        if (idempotencyKey.length() > 255) {
            return false;
        }
        
        // 영숫자, 하이픈, 언더스코어만 허용
        return idempotencyKey.matches("^[a-zA-Z0-9\\-_]+$");
    }
    
    /**
     * 멱등성 키를 생성합니다.
     * 클라이언트에서 제공하지 않은 경우 사용합니다.
     */
    public String generateIdempotencyKey(String userId, String productId, long timestamp) {
        return String.format("%s_%s_%d_%s", 
                userId, productId, timestamp, UUID.randomUUID().toString().substring(0, 8));
    }
    
    /**
     * 멱등성 키의 TTL(Time To Live)을 결정합니다.
     */
    public Duration getIdempotencyTtl() {
        return Duration.ofHours(24); // 24시간 유지
    }
    
    /**
     * 처리 중 상태의 TTL을 결정합니다.
     */
    public Duration getProcessingTtl() {
        return Duration.ofMinutes(10); // 10분 유지
    }
    
    /**
     * 동일한 멱등성 키로 다른 요청이 들어왔을 때의 처리 방식을 결정합니다.
     */
    public ConflictResolution resolveConflict(String existingRequestHash, String newRequestHash) {
        if (existingRequestHash.equals(newRequestHash)) {
            return ConflictResolution.RETURN_EXISTING_RESULT;
        } else {
            return ConflictResolution.REJECT_WITH_ERROR;
        }
    }
    
    /**
     * 요청의 해시를 생성합니다. 
     * 같은 멱등성 키지만 다른 내용의 요청을 구분하기 위해 사용합니다.
     */
    public String generateRequestHash(String userId, String productId, int quantity, String paymentMethodsHash) {
        String combined = String.format("%s_%s_%d_%s", userId, productId, quantity, paymentMethodsHash);
        return String.valueOf(combined.hashCode());
    }
    
    /**
     * 멱등성 키 재사용 방지를 위한 쿨다운 시간을 결정합니다.
     */
    public Duration getCooldownPeriod() {
        return Duration.ofSeconds(1); // 1초 쿨다운
    }
    
    /**
     * 멱등성 키가 재사용 가능한지 확인합니다.
     */
    public boolean canReuseKey(String idempotencyKey, long lastUsedTimestamp) {
        long cooldownMillis = getCooldownPeriod().toMillis();
        long currentTime = System.currentTimeMillis();
        
        return (currentTime - lastUsedTimestamp) >= cooldownMillis;
    }
    
    public enum ConflictResolution {
        RETURN_EXISTING_RESULT,  // 기존 결과 반환
        REJECT_WITH_ERROR        // 오류와 함께 거부
    }
}