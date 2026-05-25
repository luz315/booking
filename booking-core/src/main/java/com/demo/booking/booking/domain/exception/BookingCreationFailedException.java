package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingCreationFailedException extends BookingBaseException {
    
    public BookingCreationFailedException(String message, Throwable cause) {
        super(BookingErrorCode.BOOKING_CREATION_FAILED, "예약 생성에 실패했습니다: " + message, cause);
    }
}