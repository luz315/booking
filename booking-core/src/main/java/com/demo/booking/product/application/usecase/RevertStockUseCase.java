package com.demo.booking.product.application.usecase;

import java.time.LocalDate;

/**
 * 재고 원복 유스케이스
 * 점유된 재고를 원래 상태로 되돌리는 기능을 제공합니다.
 */
public interface RevertStockUseCase {
    
    /**
     * 점유된 재고를 원래 상태로 되돌립니다.
     * 예약 취소나 결제 실패 시 사용됩니다.
     * @param productId 상품 ID
     * @param checkInDate 체크인 날짜
     * @param checkOutDate 체크아웃 날짜
     * @param quantity 원복할 수량
     */
    void revertStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate, int quantity);
}