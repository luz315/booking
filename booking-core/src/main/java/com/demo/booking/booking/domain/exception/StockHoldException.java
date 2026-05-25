package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class StockHoldException extends BookingBaseException {
    
    public StockHoldException() {
        super(BookingErrorCode.STOCK_HOLD_ERROR, "Redis 재고 선점 중 오류 발생했습니다.");
    }
    
    public StockHoldException(String message) {
        super(BookingErrorCode.STOCK_HOLD_ERROR, message);
    }
}