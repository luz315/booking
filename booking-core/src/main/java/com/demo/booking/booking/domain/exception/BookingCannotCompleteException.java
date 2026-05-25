package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingCannotCompleteException extends BookingBaseException {
    
    public BookingCannotCompleteException() {
        super(BookingErrorCode.BOOKING_CANNOT_COMPLETE, "예약을 완료할 수 없습니다");
    }
}