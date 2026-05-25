package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingAlreadyConfirmedException extends BookingBaseException {
    
    public BookingAlreadyConfirmedException() {
        super(BookingErrorCode.BOOKING_ALREADY_CONFIRMED, "이미 확정된 예약입니다");
    }
}