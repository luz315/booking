package com.demo.booking.product.application.usecase;

import java.time.LocalDate;

/**
 * 재고 확정 유스케이스
 * 임시 점유된 재고를 최종 확정하는 기능을 제공합니다.
 */
public interface ConfirmStockUseCase {
    
    /**
     * 점유된 재고를 최종 확정하여 판매를 완료합니다.
     * @param productId 상품 ID
     * @param checkInDate 체크인 날짜
     * @param checkOutDate 체크아웃 날짜
     * @param quantity 확정할 수량
     */
    void confirmStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate, int quantity);
}