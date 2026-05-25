package com.demo.booking.payment.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class PaymentCannotCompleteException extends BookingBaseException {
    
    public PaymentCannotCompleteException() {
        super(PaymentErrorCode.PAYMENT_CANNOT_COMPLETE, "결제를 완료할 수 없습니다");
    }
}