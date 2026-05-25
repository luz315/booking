package com.demo.booking.payment.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class PaymentInvalidInputException extends BookingBaseException {
    
    public PaymentInvalidInputException() {
        super(PaymentErrorCode.PAYMENT_INVALID_INPUT, "결제 정보가 유효하지 않습니다");
    }
    
    public PaymentInvalidInputException(String message) {
        super(PaymentErrorCode.PAYMENT_INVALID_INPUT, message);
    }
}