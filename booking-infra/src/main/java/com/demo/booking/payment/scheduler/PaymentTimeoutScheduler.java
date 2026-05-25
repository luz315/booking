package com.demo.booking.payment.scheduler;

import com.demo.booking.payment.application.port.out.PaymentRepository;
import com.demo.booking.payment.domain.model.Payment;
import com.demo.booking.payment.domain.model.PaymentStatus;
import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.application.port.out.StockHoldPort;
import com.demo.booking.product.application.port.out.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 결제 타임아웃 처리 스케줄러
 * 20분 내 결제 미완료 시 자동 취소
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentTimeoutScheduler {

    private static final int PAYMENT_TIMEOUT_MINUTES = 20;
    
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final StockHoldPort stockHoldPort;
    private final ProductStockRepository productStockRepository;

    @Scheduled(fixedDelay = 60_000) // 1분마다 실행
    @Transactional
    public void processTimeoutPayments() {
        LocalDateTime timeoutBoundary = LocalDateTime.now().minusMinutes(PAYMENT_TIMEOUT_MINUTES);
        
        List<Payment> timeoutPayments = paymentRepository.findTimeoutPayments(
            PaymentStatus.PENDING, 
            timeoutBoundary
        );

        int timeoutCount = 0;
        for (Payment payment : timeoutPayments) {
            try {
                // 1. 결제 타임아웃 처리
                payment.timeout();
                paymentRepository.save(payment);

                // 2. 관련 예약 취소 및 재고 복구
                cancelRelatedBookingAndReleaseStock(payment);

                timeoutCount++;
                log.info("결제 타임아웃 처리 완료: paymentId={}, orderId={}", 
                        payment.getId(), payment.getOrderId());

            } catch (Exception e) {
                log.error("결제 타임아웃 처리 실패: paymentId={}, orderId={}", 
                        payment.getId(), payment.getOrderId(), e);
            }
        }

        if (timeoutCount > 0) {
            log.info("결제 타임아웃 처리 완료: 총 {}건 중 {}건 처리", 
                    timeoutPayments.size(), timeoutCount);
        }
    }

    /**
     * 결제 타임아웃으로 인한 예약 취소 및 재고 복구
     */
    private void cancelRelatedBookingAndReleaseStock(Payment payment) {
        try {
            // 예약 조회 (orderId = bookingId)
            Long bookingId = Long.parseLong(payment.getOrderId());
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalStateException("예약을 찾을 수 없습니다: " + bookingId));

            // 예약 취소 처리
            booking.markAsFailed(); // 결제 타임아웃으로 실패 처리
            bookingRepository.save(booking);

            // Redis 재고 복구
            stockHoldPort.cancelStayDates(
                    booking.getProductId(),
                    booking.getUserId(),
                    booking.getIdempotencyKey(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate()
            );
            
            // DB 재고 원복 (예약 생성 시 차감한 재고 복구)
            booking.getCheckInDate().datesUntil(booking.getCheckOutDate()).forEach(date -> {
                productStockRepository.revertStock(booking.getProductId(), date, 1);
            });

            log.info("예약 취소 및 재고 복구 완료: bookingId={}", bookingId);

        } catch (Exception e) {
            log.error("예약 취소 및 재고 복구 실패: paymentId={}, orderId={}", 
                    payment.getId(), payment.getOrderId(), e);
        }
    }
}