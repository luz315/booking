package com.demo.booking.booking.application.usecase;

import com.demo.booking.booking.domain.model.Booking;

public interface GetBookingUseCase {
    Booking getBooking(Long bookingId);
    Booking getBookingByIdempotencyKey(String idempotencyKey);
}