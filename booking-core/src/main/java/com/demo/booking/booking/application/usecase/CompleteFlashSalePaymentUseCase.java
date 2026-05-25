package com.demo.booking.booking.application.usecase;

public interface CompleteFlashSalePaymentUseCase {

    void complete(String idempotencyKey, String paymentId);
}