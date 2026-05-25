package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.port.out.StockHoldPort;
import com.demo.booking.booking.application.port.out.dto.HoldStockResult;
import com.demo.booking.booking.application.usecase.CreateFlashSaleBookingUseCase;
import com.demo.booking.booking.application.command.CreateFlashSaleBookCommand;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.exception.StockHoldException;
import com.demo.booking.booking.domain.exception.BookingCreationFailedException;
import com.demo.booking.product.application.port.out.ProductStockRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
@Named
@RequiredArgsConstructor
public class CreateFlashSaleBookingUseCaseImpl implements CreateFlashSaleBookingUseCase {

    private final StockHoldPort stockHoldPort;
    private final BookingRepository bookingRepository;
    private final ProductStockRepository productStockRepository;

    @Override
    @Transactional
    public Long createFlashSaleBooking(CreateFlashSaleBookCommand command) {
        // 1. 멱등성 키로 기존 예약 확인
        Optional<Booking> existing = bookingRepository.findByIdempotencyKey(command.idempotencyKey());
        if (existing.isPresent()) {
            return existing.get().getId();
        }

        // 2. Redis에서 재고 선점 시도 (다중 날짜 지원)
        stockHoldPort.holdStayDates(
                command.productId(),
                command.userId(),
                command.idempotencyKey(),
                command.checkInDate(),
                command.checkOutDate()
        );

        // 3. DB 트랜잭션: 재고 차감 + 예약 생성 (비관적 락)
        try {
            // 3-1. 재고 차감 (데드락 방지를 위해 날짜 순서대로 락 획득)
            List<LocalDate> sortedStayDates = command.checkInDate()
                    .datesUntil(command.checkOutDate())
                    .sorted() // 데드락 방지를 위한 정렬
                    .toList();
            
            for (LocalDate stayDate : sortedStayDates) {
                productStockRepository.holdStock(
                        command.productId(),
                        stayDate,
                        1
                );
            }

            // 3-2. 예약 생성
            Booking booking = Booking.create(
                    command.userId(),
                    command.productId(),
                    command.checkInDate(),
                    command.checkOutDate(),
                    command.totalAmount(),
                    command.idempotencyKey()
            );

            Booking savedBooking = bookingRepository.save(booking);
            return savedBooking.getId();

        } catch (StockHoldException e) {
            // 재고 부족 시 Redis 재고 복구 후 예외 전파
            stockHoldPort.cancelStayDates(
                    command.productId(),
                    command.userId(),
                    command.idempotencyKey(),
                    command.checkInDate(),
                    command.checkOutDate()
            );
            throw e;
            
        } catch (Exception e) {
            // 기타 예외 시 Redis 재고 복구
            stockHoldPort.cancelStayDates(
                    command.productId(),
                    command.userId(),
                    command.idempotencyKey(),
                    command.checkInDate(),
                    command.checkOutDate()
            );
            throw new BookingCreationFailedException("예약 생성 실패", e);
        }
    }

}