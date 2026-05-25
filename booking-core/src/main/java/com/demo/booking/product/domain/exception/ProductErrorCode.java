package com.demo.booking.product.domain.exception;

import com.demo.booking.common.exception.ErrorCode;

public enum ProductErrorCode implements ErrorCode {
    PRODUCT_NOT_FOUND,
    PRODUCT_OUT_OF_STOCK,
    PRODUCT_STOCK_INSUFFICIENT,
    PRODUCT_INACTIVE,
    PRODUCT_INVALID_PRICE,
    PRODUCT_STOCK_HOLD_FAILED,
    PRODUCT_STOCK_CONFIRM_FAILED,
    PRODUCT_STOCK_REVERT_FAILED,
    PRODUCT_STOCK_CANNOT_RESERVE,
    PRODUCT_STOCK_CANNOT_REVERT,
    PRODUCT_STOCK_CANNOT_CONFIRM,
    PRODUCT_STOCK_NOT_FOUND,
    PRODUCT_STOCK_INVALID_QUANTITY,
    PRODUCT_STOCK_INVALID_VALUE;

    @Override
    public String getCode() {
        return name();
    }
}