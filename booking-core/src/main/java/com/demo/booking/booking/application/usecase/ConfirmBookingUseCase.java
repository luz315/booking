package com.demo.booking.booking.application.usecase;

/**
 * 예약 확정 유스케이스
 * 결제가 완료된 예약을 최종 확정하는 기능을 제공합니다.
 */
public interface ConfirmBookingUseCase {
    
    /**
     * 예약을 최종 확정합니다.
     * 결제 ID를 연결하고 예약 상태를 확정으로 변경합니다.
     * @param bookingId 예약 ID
     * @param paymentId 결제 ID
     */
    void confirmBooking(Long bookingId, String paymentId);
}