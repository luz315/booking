package com.demo.booking.booking.domain.model;

import com.demo.booking.common.domain.model.Money;
import com.demo.booking.booking.domain.exception.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
public class Booking {
    private final Long id;
    private final Long userId;
    private final Long productId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Money totalAmount;
    private String paymentId;
    private BookingStatus status;
    private String idempotencyKey;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Booking(
            Long id,
            Long userId,
            Long productId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Money totalAmount,
            String paymentId,
            BookingStatus status,
            String idempotencyKey,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            ZonedDateTime deletedAt
    ) {
        // 비즈니스 불변 조건만 검증
        validateDates(checkInDate, checkOutDate);
        validateTotalAmount(totalAmount);

        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.paymentId = paymentId;
        this.status = status;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Booking create(Long userId, Long productId,
                                 LocalDate checkInDate, LocalDate checkOutDate,
                                 Money totalAmount, String idempotencyKey
    ) {
        ZonedDateTime now = ZonedDateTime.now();

        return Booking.builder()
                .userId(userId)
                .productId(productId)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .totalAmount(totalAmount)
                .paymentId(null)
                .status(BookingStatus.PENDING)
                .idempotencyKey(idempotencyKey)
                .createdAt(now)
                .updatedAt(now)
                .deletedAt(null)
                .build();
    }
    
    public static Booking restore(
            Long id, Long userId, Long productId, 
            LocalDate checkInDate, LocalDate checkOutDate,
            Money totalAmount, String paymentId, BookingStatus status, String idempotencyKey,
            ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt
    ) {
        return Booking.builder()
                .id(id)
                .userId(userId)
                .productId(productId)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .totalAmount(totalAmount)
                .paymentId(paymentId)
                .status(status)
                .idempotencyKey(idempotencyKey)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }
    
    public void attachPayment(String paymentId) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new BookingInvalidPaymentIdException();
        }
        this.paymentId = paymentId;
        this.updatedAt = ZonedDateTime.now();
    }
    
    public void confirm() {
        if (this.status != BookingStatus.PENDING) {
            throw new BookingCannotConfirmException();
        }
        // 결제 완료 여부는 외부 서비스(PaymentService)에서 검증 후 호출되어야 함
        
        this.status = BookingStatus.CONFIRMED;
        this.updatedAt = ZonedDateTime.now();
    }
    
    public void cancel() {
        if (this.status == BookingStatus.COMPLETED) {
            throw new BookingCannotCancelException();
        }
        if (this.status == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException();
        }
        
        this.status = BookingStatus.CANCELLED;
        this.updatedAt = ZonedDateTime.now();
    }
    
    public void markAsFailed() {
        if (this.status != BookingStatus.PENDING) {
            throw new BookingCannotMarkFailedException();
        }
        
        this.status = BookingStatus.FAILED;
        this.updatedAt = ZonedDateTime.now();
    }
    
    public void complete() {
        if (this.status != BookingStatus.CONFIRMED) {
            throw new BookingCannotCompleteException();
        }
        
        this.status = BookingStatus.COMPLETED;
        this.updatedAt = ZonedDateTime.now();
    }
    
    public boolean isActive() {
        return status == BookingStatus.CONFIRMED;
    }
    
    public boolean isPending() {
        return status == BookingStatus.PENDING;
    }
    
    public boolean isCancellable() {
        return status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED;
    }
    
    
    
    
    
    
    public boolean isFlashSaleReserved() {
        return status == BookingStatus.PENDING;
    }
    
    // 비즈니스 불변 조건 검증
    private static void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate.isAfter(checkOutDate)) {
            throw new BookingInvalidDatesException();
        }
        if (checkInDate.equals(checkOutDate)) {
            throw new BookingInvalidDatesException();
        }
    }
    
    private static void validateTotalAmount(Money totalAmount) {
        if (totalAmount.isZero()) {
            throw new BookingInvalidAmountException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}