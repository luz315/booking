package com.demo.booking.booking.application.port.out;

import java.time.LocalDate;

public interface StockHoldPort {

    void holdStayDates(Long productId, Long userId, String idempotencyKey,
                                       LocalDate checkInDate, LocalDate checkOutDate);

    void cancelStayDates(Long productId, Long userId, String idempotencyKey,
                        LocalDate checkInDate, LocalDate checkOutDate);
    
    int getCurrentStock(Long productId, LocalDate stayDate);

}