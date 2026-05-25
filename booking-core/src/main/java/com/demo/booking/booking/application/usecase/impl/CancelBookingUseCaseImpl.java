package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.usecase.CancelBookingUseCase;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.exception.BookingNotFoundException;
import com.demo.booking.product.application.usecase.RevertStockUseCase;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

@Named
@RequiredArgsConstructor
@Transactional
public class CancelBookingUseCaseImpl implements CancelBookingUseCase {
    
    private final BookingRepository bookingRepository;
    private final RevertStockUseCase revertStockUseCase;
    
    @Override
    public void cancelBooking(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException());
        
        // 예약 취소
        booking.cancel();
        
        // 재고 원복 (점유된 재고가 있는 경우)
        if (booking.isPending() || booking.isActive()) {
            revertStockUseCase.revertStock(booking.getProductId(), booking.getCheckInDate(), booking.getCheckOutDate(), 1);
        }
        
        bookingRepository.save(booking);
    }
}