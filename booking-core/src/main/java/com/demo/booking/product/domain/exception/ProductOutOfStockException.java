package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductOutOfStockException extends BookingBaseException {
    
    public ProductOutOfStockException() {
        super(ProductErrorCode.PRODUCT_OUT_OF_STOCK, "상품의 재고가 부족합니다");
    }
}