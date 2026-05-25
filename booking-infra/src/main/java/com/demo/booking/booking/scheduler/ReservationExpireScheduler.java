package com.demo.booking.booking.scheduler;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.port.out.StockHoldPort;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.product.application.port.out.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationExpireScheduler {

    private final BookingRepository bookingRepository;
    private final StockHoldPort stockHoldPort;
    private final ProductStockRepository productStockRepository;

    @Scheduled(fixedDelay = 30_000) // 30초마다 실행
    @Transactional
    public void expireReservations() {
        ZonedDateTime now = ZonedDateTime.now();
        List<Booking> expiredReservations = bookingRepository.findExpiredFlashSaleReservations(now);

        int expiredCount = 0;
        for (Booking booking : expiredReservations) {
            try {
                // 1. 도메인 로직으로 만료 처리
                booking.markAsFailed();
                bookingRepository.save(booking);

                // 2. Redis 재고 복구
                stockHoldPort.cancelStayDates(
                        booking.getProductId(),
                        booking.getUserId(),
                        booking.getIdempotencyKey(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate()
                );

                // 3. DB 재고 복구 (DB에서도 차감된 재고를 원복)
                List<LocalDate> stayDates = booking.getCheckInDate()
                        .datesUntil(booking.getCheckOutDate())
                        .toList();
                        
                for (LocalDate stayDate : stayDates) {
                    try {
                        productStockRepository.revertStock(
                                booking.getProductId(),
                                stayDate,
                                1
                        );
                    } catch (Exception stockException) {
                        log.warn("DB 재고 복구 실패 (계속 진행): productId={}, stayDate={}", 
                                booking.getProductId(), stayDate, stockException);
                    }
                }

                expiredCount++;

                log.info("FlashSale reservation expired and all stock released. bookingId={}, productId={}, userId={}",
                        booking.getId(), booking.getProductId(), booking.getUserId());

            } catch (Exception e) {
                log.error("Failed to expire FlashSale reservation. bookingId={}, idempotencyKey={}",
                        booking.getId(), booking.getIdempotencyKey(), e);
            }
        }

        if (expiredCount > 0) {
            log.info("FlashSale reservation expiration completed. total={}, expired={}", 
                    expiredReservations.size(), expiredCount);
        }
    }
}