package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductNotFoundException extends BookingBaseException {
    
    public ProductNotFoundException() {
        super(ProductErrorCode.PRODUCT_NOT_FOUND, "상품을 찾을 수 없습니다");
    }
}