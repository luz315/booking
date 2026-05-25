package com.demo.booking.product.application.usecase;

import com.demo.booking.product.domain.model.Product;

/**
 * 상품 조회 유스케이스
 * 상품 정보를 조회하는 기능을 제공합니다.
 */
public interface GetProductUseCase {
    
    /**
     * 상품 ID로 상품 정보를 조회합니다.
     * @param productId 상품 ID
     * @return 상품 정보
     * @throws IllegalArgumentException 상품을 찾을 수 없는 경우
     */
    Product getProduct(Long productId);
}