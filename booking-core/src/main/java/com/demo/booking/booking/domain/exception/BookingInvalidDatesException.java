package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingInvalidDatesException extends BookingBaseException {
    
    public BookingInvalidDatesException() {
        super(BookingErrorCode.BOOKING_INVALID_DATES, "날짜가 유효하지 않습니다");
    }
}