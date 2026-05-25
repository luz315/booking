package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingNotFoundException extends BookingBaseException {
    
    public BookingNotFoundException() {
        super(BookingErrorCode.BOOKING_NOT_FOUND, "예약을 찾을 수 없습니다");
    }
}