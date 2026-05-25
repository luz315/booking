package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.application.usecase.ConfirmBookingUseCase;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.exception.BookingNotFoundException;
import com.demo.booking.product.application.usecase.ConfirmStockUseCase;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

@Named
@RequiredArgsConstructor
@Transactional
public class ConfirmBookingUseCaseImpl implements ConfirmBookingUseCase {
    
    private final BookingRepository bookingRepository;
    private final ConfirmStockUseCase confirmStockUseCase;
    
    @Override
    public void confirmBooking(Long bookingId, String paymentId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException());
        
        // 결제 정보 연결
        booking.attachPayment(paymentId);
        
        // 예약 확정
        booking.confirm();
        
        // 재고 확정
        confirmStockUseCase.confirmStock(booking.getProductId(), booking.getCheckInDate(), booking.getCheckOutDate(), 1);
        
        bookingRepository.save(booking);
    }
}