package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingAlreadyCancelledException extends BookingBaseException {
    
    public BookingAlreadyCancelledException() {
        super(BookingErrorCode.BOOKING_ALREADY_CANCELLED, "이미 취소된 예약입니다");
    }
}