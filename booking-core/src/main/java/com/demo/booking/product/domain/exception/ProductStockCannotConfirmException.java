package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductStockCannotConfirmException extends BookingBaseException {
    
    public ProductStockCannotConfirmException() {
        super(ProductErrorCode.PRODUCT_STOCK_CANNOT_CONFIRM, "재고 예약을 확정할 수 없습니다");
    }
}