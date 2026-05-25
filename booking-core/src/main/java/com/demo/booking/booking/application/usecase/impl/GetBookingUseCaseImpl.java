package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.usecase.GetBookingUseCase;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.exception.BookingNotFoundException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;

@Named
@RequiredArgsConstructor
public class GetBookingUseCaseImpl implements GetBookingUseCase {
    
    private final BookingRepository bookingRepository;
    
    @Override
    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException());
    }
    
    @Override
    public Booking getBookingByIdempotencyKey(String idempotencyKey) {
        return bookingRepository.findByIdempotencyKey(idempotencyKey)
                .orElseThrow(() -> new BookingNotFoundException());
    }
}