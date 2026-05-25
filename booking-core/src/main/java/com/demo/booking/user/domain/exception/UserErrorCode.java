package com.demo.booking.user.domain.exception;

import com.demo.booking.common.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND,
    INSUFFICIENT_POINTS,
    INVALID_POINT_AMOUNT,
    POINT_TRANSACTION_FAILED,
    INVALID_POINT_VALUE;

    @Override
    public String getCode() {
        return name();
    }
}