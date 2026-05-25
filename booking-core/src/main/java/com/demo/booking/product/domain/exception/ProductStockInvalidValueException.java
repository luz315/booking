package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductStockInvalidValueException extends BookingBaseException {
    
    public ProductStockInvalidValueException() {
        super(ProductErrorCode.PRODUCT_STOCK_INVALID_VALUE, "재고 값이 유효하지 않습니다");
    }
}