package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductInvalidInputException extends BookingBaseException {
    
    public ProductInvalidInputException() {
        super(ProductErrorCode.PRODUCT_STOCK_INVALID_VALUE, "입력 값이 유효하지 않습니다");
    }
}