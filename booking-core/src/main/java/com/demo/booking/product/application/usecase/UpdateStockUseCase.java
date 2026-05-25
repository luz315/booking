package com.demo.booking.product.application.usecase;

import java.time.LocalDate;

/**
 * 재고 업데이트 유스케이스
 * 상품의 총 재고 수량을 변경하는 기능을 제공합니다.
 */
public interface UpdateStockUseCase {
    
    /**
     * 지정된 기간의 상품 총 재고를 업데이트합니다.
     * @param productId 상품 ID
     * @param checkInDate 체크인 날짜
     * @param checkOutDate 체크아웃 날짜
     * @param newTotalStock 새로운 총 재고 수량
     */
    void updateStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate, int newTotalStock);
}