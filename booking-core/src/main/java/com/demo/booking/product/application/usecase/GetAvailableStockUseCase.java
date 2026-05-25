package com.demo.booking.product.application.usecase;

import com.demo.booking.product.domain.model.ProductStock;

import java.time.LocalDate;
import java.util.List;

/**
 * 상품 재고 조회 유스케이스
 * 예약 가능한 상품의 재고 정보를 조회하는 기능을 제공합니다.
 */
public interface GetAvailableStockUseCase {
    
    /**
     * 지정된 기간의 상품 재고 정보를 조회합니다.
     * 체크인부터 체크아웃 전날까지 모든 날짜의 예약 가능한 재고 목록을 반환합니다.
     * @param productId 상품 ID
     * @param checkInDate 체크인 날짜
     * @param checkOutDate 체크아웃 날짜
     * @return 기간 내 예약 가능한 재고 목록
     */
    List<ProductStock> getAvailableStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate);
}