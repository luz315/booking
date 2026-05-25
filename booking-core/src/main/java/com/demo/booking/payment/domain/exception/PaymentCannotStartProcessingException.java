package com.demo.booking.payment.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class PaymentCannotStartProcessingException extends BookingBaseException {
    
    public PaymentCannotStartProcessingException() {
        super(PaymentErrorCode.PAYMENT_CANNOT_START_PROCESSING, "결제 처리를 시작할 수 없습니다");
    }
}