package com.demo.booking.booking.application.usecase;

public interface FailFlashSalePaymentUseCase {

    void fail(String idempotencyKey);
}