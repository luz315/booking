package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class ProductStockCannotRevertException extends BookingBaseException {
    
    public ProductStockCannotRevertException() {
        super(ProductErrorCode.PRODUCT_STOCK_CANNOT_REVERT, "재고 예약을 취소할 수 없습니다");
    }
}