package com.demo.booking.payment.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class PaymentFailedException extends BookingBaseException {
    
    public PaymentFailedException() {
        super(PaymentErrorCode.PAYMENT_FAILED, "결제에 실패했습니다");
    }
    
    public PaymentFailedException(String message) {
        super(PaymentErrorCode.PAYMENT_FAILED, message);
    }
}