package com.demo.booking.user.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class InsufficientPointsException extends BookingBaseException {
    
    public InsufficientPointsException() {
        super(UserErrorCode.INSUFFICIENT_POINTS, "포인트가 부족합니다");
    }
}