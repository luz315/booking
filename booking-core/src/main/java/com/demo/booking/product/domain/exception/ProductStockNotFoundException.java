package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductStockNotFoundException extends BookingBaseException {
    
    public ProductStockNotFoundException() {
        super(ProductErrorCode.PRODUCT_STOCK_NOT_FOUND, "상품 재고 정보를 찾을 수 없습니다");
    }
}