package com.demo.booking.product.application.usecase;

import java.time.LocalDate;

/**
 * 재고 점유 유스케이스
 * 예약을 위해 상품 재고를 임시로 점유하는 기능을 제공합니다.
 */
public interface HoldStockUseCase {
    
    /**
     * 지정된 기간의 모든 날짜에 상품 재고를 임시 점유합니다.
     * @param productId 상품 ID
     * @param checkInDate 체크인 날짜
     * @param checkOutDate 체크아웃 날짜
     * @param quantity 점유할 수량
     * @return 점유 성공 여부 (모든 날짜에서 성공해야 true)
     */
    boolean holdStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate, int quantity);
}