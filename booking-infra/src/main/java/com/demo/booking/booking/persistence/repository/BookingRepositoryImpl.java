package com.demo.booking.booking.persistence.repository;

import com.demo.booking.booking.application.port.out.BookingRepository;
import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.model.BookingStatus;
import com.demo.booking.booking.persistence.entity.BookingRecord;
import com.demo.booking.booking.persistence.mapper.BookingMapper;
import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;
import com.demo.booking.common.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {
    
    private final BookingJpaRepository bookingJpaRepository;
    
    @Override
    public Booking save(Booking booking) {
        BookingRecord record = BookingMapper.toRecord(booking);
        BookingRecord savedRecord = bookingJpaRepository.save(record);
        return BookingMapper.toDomain(savedRecord);
    }
    
    @Override
    public Optional<Booking> findById(Long id) {
        return bookingJpaRepository.findByIdAndDeletedAtIsNull(id)
                .map(BookingMapper::toDomain);
    }
    
    @Override
    public Optional<Booking> findByIdempotencyKey(String idempotencyKey) {
        return bookingJpaRepository.findByIdempotencyKeyAndDeletedAtIsNull(idempotencyKey)
                .map(BookingMapper::toDomain);
    }
    
    @Override
    public List<Booking> findByUserId(Long userId) {
        return bookingJpaRepository.findByUserIdAndDeletedAtIsNull(userId)
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }
    
    @Override
    public List<Booking> findByProductId(Long productId) {
        return bookingJpaRepository.findByProductIdAndDeletedAtIsNull(productId)
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }
    
    @Override
    public PageResult<Booking> findByUserId(Long userId, PageCriteria pageCriteria) {
        Pageable pageable = PageableUtils.createPageable(pageCriteria);
        Page<BookingRecord> page = bookingJpaRepository.findByUserIdAndDeletedAtIsNull(userId, pageable);
        
        List<Booking> bookings = page.getContent().stream()
                .map(BookingMapper::toDomain)
                .toList();
                
        return new PageResult<>(
                bookings,
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
    
    @Override
    public boolean existsById(Long id) {
        return bookingJpaRepository.existsByIdAndDeletedAtIsNull(id);
    }
    
    @Override
    public boolean existsByIdempotencyKey(String idempotencyKey) {
        return bookingJpaRepository.existsByIdempotencyKeyAndDeletedAtIsNull(idempotencyKey);
    }
    
    // FlashSale 관련 메서드 구현
    @Override
    public List<Booking> findExpiredFlashSaleReservations(ZonedDateTime now) {
        return bookingJpaRepository
                .findByStatusAndFlashSaleExpiresAtIsNotNullAndFlashSaleExpiresAtBeforeAndDeletedAtIsNull(
                        BookingStatus.PENDING, now)
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }
    
    @Override
    public Optional<Booking> findByProductIdAndUserIdAndStatus(Long productId, Long userId, BookingStatus status) {
        return bookingJpaRepository
                .findByProductIdAndUserIdAndStatusAndDeletedAtIsNull(productId, userId, status)
                .map(BookingMapper::toDomain);
    }
    
    @Override
    public List<Booking> findByStatusAndFlashSaleExpiresAtBefore(BookingStatus status, ZonedDateTime expiresAt) {
        return bookingJpaRepository
                .findByStatusAndFlashSaleExpiresAtBeforeAndDeletedAtIsNull(status, expiresAt)
                .stream()
                .map(BookingMapper::toDomain)
                .toList();
    }
}