package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductStockCannotReserveException extends BookingBaseException {
    
    public ProductStockCannotReserveException() {
        super(ProductErrorCode.PRODUCT_STOCK_CANNOT_RESERVE, "재고를 예약할 수 없습니다");
    }
}