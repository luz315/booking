package com.demo.booking.payment.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class PaymentCannotCancelException extends BookingBaseException {
    
    public PaymentCannotCancelException() {
        super(PaymentErrorCode.PAYMENT_CANNOT_CANCEL, "결제를 취소할 수 없습니다");
    }
}