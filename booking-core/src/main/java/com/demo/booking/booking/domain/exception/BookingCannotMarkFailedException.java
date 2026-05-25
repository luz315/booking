package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingCannotMarkFailedException extends BookingBaseException {
    
    public BookingCannotMarkFailedException() {
        super(BookingErrorCode.BOOKING_CANNOT_MARK_FAILED, "예약을 실패 상태로 변경할 수 없습니다");
    }
}