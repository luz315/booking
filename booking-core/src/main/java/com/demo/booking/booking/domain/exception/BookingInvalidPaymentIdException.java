package com.demo.booking.booking.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class BookingInvalidPaymentIdException extends BookingBaseException {
    
    public BookingInvalidPaymentIdException() {
        super(BookingErrorCode.BOOKING_INVALID_PAYMENT_ID, "결제 ID는 필수입니다");
    }
}