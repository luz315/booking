package com.demo.booking.booking.application.port.out;

import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.model.BookingStatus;
import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    
    Booking save(Booking booking);
    
    Optional<Booking> findById(Long id);
    
    Optional<Booking> findByIdempotencyKey(String idempotencyKey);
    
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByProductId(Long productId);
    
    PageResult<Booking> findByUserId(Long userId, PageCriteria pageCriteria);
    
    boolean existsById(Long id);
    
    boolean existsByIdempotencyKey(String idempotencyKey);
    
    // FlashSale 관련 메서드들
    List<Booking> findExpiredFlashSaleReservations(ZonedDateTime now);
    
    Optional<Booking> findByProductIdAndUserIdAndStatus(Long productId, Long userId, BookingStatus status);
    
    List<Booking> findByStatusAndFlashSaleExpiresAtBefore(BookingStatus status, ZonedDateTime expiresAt);
}