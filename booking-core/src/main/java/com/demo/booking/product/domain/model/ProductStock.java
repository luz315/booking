package com.demo.booking.product.domain.model;

import com.demo.booking.product.domain.exception.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ProductStock {
    private final Long id;
    private final Long productId;
    private LocalDate stayDate;
    private int totalStock;
    private int availableStock;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime deletedAt;

    
    /**
     * 재고를 점유합니다 (임시 예약)
     */
    public void hold(int quantity) {
        if (quantity <= 0) {
            throw new ProductStockInvalidQuantityException();
        }
        
        if (availableStock < quantity) {
            throw new ProductOutOfStockException();
        }

        this.availableStock -= quantity;
        this.updatedAt = ZonedDateTime.now();
    }
    
    /**
     * 점유된 재고를 원복합니다
     */
    public void revertHold(int quantity) {
        if (quantity <= 0) {
            throw new ProductStockInvalidQuantityException();
        }

        this.availableStock += quantity;
        this.updatedAt = ZonedDateTime.now();
    }
    
    /**
     * 점유된 재고를 확정합니다 (점유 → 판매 완료)
     */
    public void confirmHold(int quantity) {
        if (quantity <= 0) {
            throw new ProductStockInvalidQuantityException();
        }

        // availableStock는 그대로 (이미 점유 시 차감됨)
        this.updatedAt = ZonedDateTime.now();
    }
    
    /**
     * 총 재고 수량을 업데이트합니다.
     */
    public void updateTotalStock(int newTotalStock) {
        if (newTotalStock < 0) {
            throw new ProductStockInvalidValueException();
        }

        int difference = newTotalStock - this.totalStock;
        this.totalStock = newTotalStock;
        this.availableStock += difference;
        this.updatedAt = ZonedDateTime.now();
    }
    
    /**
     * 해당 날짜에 점유 가능한지 확인합니다.
     */
    public boolean canHold(int quantity) {
        return availableStock >= quantity && quantity > 0;
    }

    /**
     * 해당 날짜가 예약 가능한 상태인지 확인합니다.
     */
    public boolean isAvailable() {
        return availableStock > 0;
    }
    
    // 비즈니스 불변 조건 검증
    private static void validateQuantities(int totalStock) {
        if (totalStock < 0) {
            throw new ProductStockInvalidValueException();
        }
    }
    
    public static ProductStock restore(Long id, Long productId, LocalDate stayDate, 
                                     int totalStock, int availableStock, 
                                     ZonedDateTime createdAt, ZonedDateTime updatedAt, 
                                     ZonedDateTime deletedAt) {
        return ProductStock.builder()
                .id(id)
                .productId(productId)
                .stayDate(stayDate)
                .totalStock(totalStock)
                .availableStock(availableStock)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductStock that = (ProductStock) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}