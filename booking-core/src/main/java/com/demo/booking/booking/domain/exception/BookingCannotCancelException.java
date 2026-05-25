package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingCannotCancelException extends BookingBaseException {
    
    public BookingCannotCancelException() {
        super(BookingErrorCode.BOOKING_CANNOT_CANCEL, "예약을 취소할 수 없습니다");
    }
}