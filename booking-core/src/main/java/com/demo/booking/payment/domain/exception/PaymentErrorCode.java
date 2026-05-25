package com.demo.booking.payment.domain.exception;

import com.demo.booking.common.exception.ErrorCode;

public enum PaymentErrorCode implements ErrorCode {
    PAYMENT_NOT_FOUND,
    PAYMENT_FAILED,
    PAYMENT_ALREADY_PROCESSED,
    PAYMENT_AMOUNT_MISMATCH,
    PAYMENT_METHOD_INVALID,
    PAYMENT_GATEWAY_ERROR,
    REFUND_FAILED,
    REFUND_AMOUNT_INVALID,
    PAYMENT_CANNOT_START_PROCESSING,
    PAYMENT_CANNOT_COMPLETE,
    PAYMENT_CANNOT_FAIL,
    PAYMENT_CANNOT_CANCEL,
    PAYMENT_ALREADY_CANCELLED,
    PAYMENT_INVALID_INPUT;

    @Override
    public String getCode() {
        return name();
    }
}