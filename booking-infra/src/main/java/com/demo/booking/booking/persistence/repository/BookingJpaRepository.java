package com.demo.booking.booking.persistence.repository;

import com.demo.booking.booking.domain.model.BookingStatus;
import com.demo.booking.booking.persistence.entity.BookingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepository extends JpaRepository<BookingRecord, Long> {

    Optional<BookingRecord> findByIdempotencyKey(String idempotencyKey);
    
    List<BookingRecord> findByUserId(Long userId);
    
    List<BookingRecord> findByProductId(Long productId);
    
    Page<BookingRecord> findByUserId(Long userId, Pageable pageable);
    
    List<BookingRecord> findByStatus(BookingStatus status);
    
    List<BookingRecord> findByCheckInDate(LocalDate checkInDate);
    
    List<BookingRecord> findByUserIdAndDeletedAtIsNull(Long userId);
    
    Page<BookingRecord> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);
    
    List<BookingRecord> findByProductIdAndDeletedAtIsNull(Long productId);
    
    Optional<BookingRecord> findByIdAndDeletedAtIsNull(Long id);
    
    Optional<BookingRecord> findByIdempotencyKeyAndDeletedAtIsNull(String idempotencyKey);
    
    Page<BookingRecord> findByDeletedAtIsNull(Pageable pageable);
    
    boolean existsByIdAndDeletedAtIsNull(Long id);
    
    boolean existsByIdempotencyKeyAndDeletedAtIsNull(String idempotencyKey);
    
    List<BookingRecord> findByUserIdAndProductIdAndCheckInDateAndStatusInAndDeletedAtIsNull(
            Long userId,
            Long productId,
            LocalDate checkInDate,
            List<BookingStatus> statuses
    );
    
    // FlashSale 관련 쿼리들
    List<BookingRecord> findByStatusAndFlashSaleExpiresAtBeforeAndDeletedAtIsNull(
            BookingStatus status, 
            ZonedDateTime expiresAt
    );
    
    Optional<BookingRecord> findByProductIdAndUserIdAndStatusAndDeletedAtIsNull(
            Long productId, 
            Long userId, 
            BookingStatus status
    );
    
    List<BookingRecord> findByStatusAndFlashSaleExpiresAtIsNotNullAndFlashSaleExpiresAtBeforeAndDeletedAtIsNull(
            BookingStatus status, 
            ZonedDateTime currentTime
    );
}