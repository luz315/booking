package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.usecase.CompleteFlashSalePaymentUseCase;
import com.demo.booking.booking.domain.model.Booking;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
@Named
@RequiredArgsConstructor
@Transactional
public class CompleteFlashSalePaymentUseCaseImpl implements CompleteFlashSalePaymentUseCase {

    private final BookingRepository bookingRepository;

    @Override
    public void complete(String idempotencyKey, String paymentId) {
        Booking booking = bookingRepository.findByIdempotencyKey(idempotencyKey)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다: " + idempotencyKey));

        // 결제 ID 설정 및 예약 확정
        booking.attachPayment(paymentId);
        booking.confirm();
        
        bookingRepository.save(booking);
    }
}