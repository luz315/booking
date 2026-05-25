package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductStockInvalidQuantityException extends BookingBaseException {
    
    public ProductStockInvalidQuantityException() {
        super(ProductErrorCode.PRODUCT_STOCK_INVALID_QUANTITY, "수량이 유효하지 않습니다");
    }
}