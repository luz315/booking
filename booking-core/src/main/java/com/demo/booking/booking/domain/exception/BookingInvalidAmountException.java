package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingInvalidAmountException extends BookingBaseException {
    
    public BookingInvalidAmountException() {
        super(BookingErrorCode.BOOKING_INVALID_AMOUNT, "금액이 유효하지 않습니다");
    }
}