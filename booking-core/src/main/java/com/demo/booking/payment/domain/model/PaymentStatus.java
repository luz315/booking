package com.demo.booking.payment.domain.model;

public enum PaymentStatus {
    PENDING,      // 결제 대기
    PROCESSING,   // 결제 처리 중
    COMPLETED,    // 결제 완료
    FAILED,       // 결제 실패
    CANCELLED,    // 결제 취소
    TIMEOUT       // 결제 타임아웃 (20분 경과)
}