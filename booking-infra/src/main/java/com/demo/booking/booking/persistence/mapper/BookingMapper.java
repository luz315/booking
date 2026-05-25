package com.demo.booking.booking.persistence.mapper;

import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.persistence.entity.BookingRecord;
import com.demo.booking.common.domain.model.Money;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {
    
    public static Booking toDomain(BookingRecord record) {
        return Booking.restore(
                record.getId(),
                record.getUserId(),
                record.getProductId(),
                record.getCheckInDate(),
                record.getCheckOutDate(),
                Money.of(record.getTotalAmount()),
                record.getPaymentId(),
                record.getStatus(),
                record.getIdempotencyKey(),
                record.getCreatedAt(),
                record.getUpdatedAt(),
                record.getDeletedAt()
        );
    }
    
    public static BookingRecord toRecord(Booking domain) {
        return new BookingRecord(
                domain.getId(),
                domain.getUserId(),
                domain.getProductId(),
                domain.getCheckInDate(),
                domain.getCheckOutDate(),
                domain.getTotalAmount().getAmount(),
                domain.getPaymentId(),
                domain.getStatus(),
                domain.getIdempotencyKey(),
                null, // flashSaleExpiresAt - 도메인에서 제거됨
                domain.getCreatedAt(),
                domain.getUpdatedAt(),
                domain.getDeletedAt()
        );
    }
}