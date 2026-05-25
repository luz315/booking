package com.demo.booking.payment.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class PaymentCannotFailException extends BookingBaseException {
    
    public PaymentCannotFailException() {
        super(PaymentErrorCode.PAYMENT_CANNOT_FAIL, "결제를 실패 상태로 변경할 수 없습니다");
    }
}