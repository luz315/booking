package com.demo.booking.product.domain.service;

import com.demo.booking.product.domain.model.Product;
import com.demo.booking.product.domain.model.ProductStock;
import com.demo.booking.product.domain.exception.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

public class ProductStockPolicy {
    
    /**
     * 재고 예약을 위한 락 키를 생성합니다.
     */
    public String generateLockKey(String productId, String userId) {
        return String.format("inventory_lock_%s_%s_%s", productId, userId, UUID.randomUUID().toString());
    }
    
    /**
     * 재고 락 대기 시간을 결정합니다.
     */
    public Duration getLockTimeout() {
        return Duration.ofSeconds(10); // 10초 대기
    }
    
    /**
     * 재고 락 유지 시간을 결정합니다.
     */
    public Duration getLockHoldTime() {
        return Duration.ofMinutes(10); // 10분 유지
    }
    
    /**
     * 상품의 재고 예약 가능 여부를 검증합니다.
     */
    public boolean canReserveStock(Product product, ProductStock stock, int requestedQuantity, LocalDate stayDate) {
        if (product == null) {
            throw new ProductInvalidInputException();
        }
        if (stock == null) {
            throw new ProductInvalidInputException();
        }
        if (requestedQuantity <= 0) {
            throw new ProductStockInvalidQuantityException();
        }
        
        return stock.isAvailable() && stock.canHold(requestedQuantity);
    }
    
    /**
     * 동시 요청 시 재고 예약 우선순위를 결정합니다.
     * 현재는 단순히 요청 순서대로 처리하지만, 향후 다른 정책을 적용할 수 있습니다.
     */
    public int calculatePriority(String userId, int requestedQuantity) {
        // 기본 우선순위는 0 (낮을수록 높은 우선순위)
        // 향후 VIP 회원, 요청 수량 등을 고려한 우선순위 로직 추가 가능
        return 0;
    }
    
    /**
     * 재고 부족 시 대안 상품 추천 여부를 결정합니다.
     */
    public boolean shouldSuggestAlternatives(ProductStock stock, int requestedQuantity) {
        return stock.isAvailable() && stock.getAvailableStock() < requestedQuantity;
    }
    
    /**
     * 재고 예약 실패 시 재시도 여부를 결정합니다.
     */
    public boolean shouldRetry(int attemptCount, Throwable lastError) {
        return attemptCount < 3 && 
               (lastError instanceof IllegalStateException || 
                lastError.getMessage().contains("lock"));
    }
    
    /**
     * 재시도 대기 시간을 계산합니다.
     */
    public Duration getRetryDelay(int attemptCount) {
        // Exponential backoff: 100ms, 200ms, 400ms
        return Duration.ofMillis(100L * (1L << attemptCount));
    }
}