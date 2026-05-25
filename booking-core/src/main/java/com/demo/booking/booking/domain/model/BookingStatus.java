package com.demo.booking.booking.domain.model;

public enum BookingStatus {
    PENDING,           // 결제 대기 (일반 예약 & 플래시 세일 공통)
    CONFIRMED,         // 결제 완료 및 예약 확정
    CANCELLED,         // 예약 취소
    COMPLETED,         // 이용 완료
    FAILED            // 결제 실패
}