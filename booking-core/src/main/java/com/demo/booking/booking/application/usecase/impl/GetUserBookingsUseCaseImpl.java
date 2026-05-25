package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.usecase.GetUserBookingsUseCase;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequiredArgsConstructor
public class GetUserBookingsUseCaseImpl implements GetUserBookingsUseCase {
    
    private final BookingRepository bookingRepository;
    
    @Override
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    @Override
    public PageResult<Booking> getUserBookings(Long userId, PageCriteria pageCriteria) {
        return bookingRepository.findByUserId(userId, pageCriteria);
    }
}