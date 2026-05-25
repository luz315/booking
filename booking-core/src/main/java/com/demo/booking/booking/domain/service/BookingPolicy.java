package com.demo.booking.booking.domain.service;

import com.demo.booking.booking.domain.model.Booking;
import com.demo.booking.booking.domain.model.BookingStatus;
import com.demo.booking.common.domain.model.Money;
import java.time.Duration;
import java.time.LocalDateTime;

public class BookingPolicy {
    
    /**
     * 예약 가능 시간을 확인합니다.
     */
    public boolean isBookingTimeValid(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        if (checkInTime == null || checkOutTime == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // 체크인 시간은 현재 시간 이후여야 함
        if (checkInTime.isBefore(now)) {
            return false;
        }
        
        // 체크인이 체크아웃보다 먼저여야 함
        if (checkInTime.isAfter(checkOutTime) || checkInTime.equals(checkOutTime)) {
            return false;
        }
        
        // 최소 1시간 이상의 예약 시간이 필요
        Duration minDuration = Duration.ofHours(1);
        return Duration.between(checkInTime, checkOutTime).compareTo(minDuration) >= 0;
    }
    
    /**
     * 예약 취소 가능 여부를 확인합니다.
     */
    public boolean isCancellable(Booking booking) {
        if (booking == null) {
            return false;
        }
        
        // 완료된 예약은 취소 불가
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            return false;
        }
        
        // 이미 취소된 예약은 취소 불가
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return false;
        }
        
        return booking.isCancellable();
    }
    
    /**
     * 취소 수수료를 계산합니다.
     */
    public Money calculateCancellationFee(Booking booking, LocalDateTime cancellationTime) {
        if (booking == null || cancellationTime == null) {
            return Money.zero();
        }
        
        LocalDateTime createdAt = booking.getCreatedAt().toLocalDateTime();
        Duration timeSinceBooking = Duration.between(createdAt, cancellationTime);
        
        Money totalAmount = booking.getTotalAmount();
        
        // 예약 후 1시간 이내: 무료 취소
        if (timeSinceBooking.toHours() < 1) {
            return Money.zero();
        }
        
        // 예약 후 24시간 이내: 10% 수수료
        if (timeSinceBooking.toHours() < 24) {
            return totalAmount.multiply(java.math.BigDecimal.valueOf(0.1));
        }
        
        // 예약 후 24시간 이후: 20% 수수료
        return totalAmount.multiply(java.math.BigDecimal.valueOf(0.2));
    }
    
    /**
     * 최대 예약 수량을 결정합니다.
     */
    public int getMaxBookingQuantity() {
        return 10; // 한 번에 최대 10개까지 예약 가능
    }
    
    /**
     * 예약 확정까지의 대기 시간을 결정합니다.
     */
    public Duration getBookingConfirmationTimeout() {
        return Duration.ofMinutes(15); // 15분 내에 결제 완료되어야 함
    }
    
    /**
     * 예약이 자동 취소되어야 하는지 확인합니다.
     */
    public boolean shouldAutoCancel(Booking booking) {
        if (booking == null || booking.getStatus() != BookingStatus.PENDING) {
            return false;
        }
        
        LocalDateTime createdAt = booking.getCreatedAt().toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        Duration elapsed = Duration.between(createdAt, now);
        
        return elapsed.compareTo(getBookingConfirmationTimeout()) > 0;
    }
    
    /**
     * 사용자당 동시 예약 가능한 개수를 제한합니다.
     */
    public int getMaxConcurrentBookings() {
        return 5; // 사용자당 최대 5개의 활성 예약
    }
    
    /**
     * 예약 우선순위를 계산합니다.
     */
    public int calculateBookingPriority(String userId, boolean isVip, int loyaltyLevel) {
        int priority = 0;
        
        if (isVip) {
            priority += 100;
        }
        
        priority += loyaltyLevel * 10;
        
        return priority;
    }
    
    /**
     * 예약 대기열 진입 여부를 결정합니다.
     */
    public boolean shouldEnterWaitingQueue(int availableQuantity, int requestedQuantity) {
        // 재고가 부족하더라도 대기열에 진입 가능
        return availableQuantity > 0 || requestedQuantity <= getMaxBookingQuantity();
    }
    
    /**
     * 대기열에서의 최대 대기 시간을 결정합니다.
     */
    public Duration getMaxWaitingTime() {
        return Duration.ofMinutes(30); // 최대 30분 대기
    }
}