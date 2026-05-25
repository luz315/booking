package com.demo.booking.booking.application.usecase;

/**
 * 예약 취소 유스케이스
 * 기존 예약을 취소하고 재고를 원복하는 기능을 제공합니다.
 */
public interface CancelBookingUseCase {
    
    /**
     * 예약을 취소합니다.
     * 예약 상태를 취소로 변경하고 점유된 재고를 원복합니다.
     * @param bookingId 예약 ID
     * @param reason 취소 사유
     */
    void cancelBooking(Long bookingId, String reason);
}