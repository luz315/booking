package com.demo.booking.booking.application.support;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.exception.BookingNotFoundException;
import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;

import jakarta.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class BookingFinder {
    
    private final BookingRepository bookingRepository;
    
    public BookingFinder(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    
    
    /**
     * 멱등성 키로 기존 예약을 조회합니다.
     */
    public Optional<Booking> findByIdempotencyKey(String idempotencyKey) {
        return bookingRepository.findByIdempotencyKey(idempotencyKey);
    }
    
    
    
    
    
    /**
     * 멱등성 키가 이미 사용되었는지 확인합니다.
     */
    public boolean existsByIdempotencyKey(String idempotencyKey) {
        return bookingRepository.existsByIdempotencyKey(idempotencyKey);
    }
}