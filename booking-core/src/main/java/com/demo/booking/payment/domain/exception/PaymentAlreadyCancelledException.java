package com.demo.booking.payment.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class PaymentAlreadyCancelledException extends BookingBaseException {
    
    public PaymentAlreadyCancelledException() {
        super(PaymentErrorCode.PAYMENT_ALREADY_CANCELLED, "이미 취소된 결제입니다");
    }
}