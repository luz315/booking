package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.usecase.CompleteBookingUseCase;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.exception.BookingNotFoundException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

@Named
@RequiredArgsConstructor
@Transactional
public class CompleteBookingUseCaseImpl implements CompleteBookingUseCase {
    
    private final BookingRepository bookingRepository;
    
    @Override
    public void completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException());
        
        // 예약 완료
        booking.complete();
        
        bookingRepository.save(booking);
    }
}