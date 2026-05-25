package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingCannotConfirmException extends BookingBaseException {
    
    public BookingCannotConfirmException() {
        super(BookingErrorCode.BOOKING_CANNOT_CONFIRM, "예약을 확정할 수 없습니다");
    }
}