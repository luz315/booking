package com.demo.booking.payment.domain.service;

import com.demo.booking.payment.domain.model.PaymentMethod;
import com.demo.booking.payment.domain.model.PaymentMethod.PaymentType;

/**
 * 결제 수단별 처리 전략 인터페이스
 * 새로운 결제 수단 추가 시 이 인터페이스만 구현하면 됨
 */
public interface PaymentProcessor {
    
    /**
     * 지원하는 결제 수단 타입
     */
    PaymentType getSupportedType();
    
    /**
     * 결제 처리
     * @param paymentMethod 결제 수단 정보
     * @param transactionId 거래 ID
     * @return 결제 결과 (성공 시 외부 결제 시스템의 거래 ID)
     */
    PaymentResult process(PaymentMethod paymentMethod, String transactionId);
    
    /**
     * 결제 취소
     * @param paymentMethod 결제 수단 정보  
     * @param transactionId 거래 ID
     * @param externalTransactionId 외부 결제 시스템의 거래 ID
     */
    void cancel(PaymentMethod paymentMethod, String transactionId, String externalTransactionId);
    
    /**
     * 결제 결과
     */
    record PaymentResult(
        boolean success,
        String externalTransactionId,
        String message
    ) {
        public static PaymentResult success(String externalTransactionId) {
            return new PaymentResult(true, externalTransactionId, "결제 성공");
        }
        
        public static PaymentResult failure(String message) {
            return new PaymentResult(false, null, message);
        }
    }
}