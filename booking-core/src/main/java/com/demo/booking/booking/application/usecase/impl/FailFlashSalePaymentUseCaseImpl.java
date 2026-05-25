package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.port.out.StockHoldPort;
import com.demo.booking.booking.application.usecase.FailFlashSalePaymentUseCase;
import com.demo.booking.booking.domain.model.Booking;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
@Named
@RequiredArgsConstructor
@Transactional
public class FailFlashSalePaymentUseCaseImpl implements FailFlashSalePaymentUseCase {

    private final BookingRepository bookingRepository;
    private final StockHoldPort stockHoldPort;

    @Override
    public void fail(String idempotencyKey) {
        Booking booking = bookingRepository.findByIdempotencyKey(idempotencyKey)
                .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다: " + idempotencyKey));

        // 결제 실패 처리
        booking.markAsFailed();
        bookingRepository.save(booking);

        // Redis 재고 복구
        stockHoldPort.cancelStayDates(
                booking.getProductId(),
                booking.getUserId(),
                booking.getIdempotencyKey(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );
    }
}