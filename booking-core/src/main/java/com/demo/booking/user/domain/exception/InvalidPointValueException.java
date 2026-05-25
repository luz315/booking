package com.demo.booking.user.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class InvalidPointValueException extends BookingBaseException {
    
    public InvalidPointValueException() {
        super(UserErrorCode.INVALID_POINT_VALUE, "포인트 값이 유효하지 않습니다");
    }
}