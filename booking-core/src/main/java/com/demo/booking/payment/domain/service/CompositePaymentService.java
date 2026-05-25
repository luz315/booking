package com.demo.booking.payment.domain.service;

import com.demo.booking.payment.domain.model.Payment;
import com.demo.booking.payment.domain.model.PaymentMethod;
import com.demo.booking.payment.domain.model.PaymentMethod.PaymentType;
import com.demo.booking.payment.domain.exception.PaymentFailedException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 복합 결제 처리 서비스
 * 여러 결제 수단을 조합하여 처리하는 핵심 비즈니스 로직
 */
@RequiredArgsConstructor
public class CompositePaymentService {
    
    private final List<PaymentProcessor> paymentProcessors;
    
    /**
     * 복합 결제 처리
     */
    public void processPayment(Payment payment) {
        // Payment 도메인 객체가 이미 생성 시점에 검증 완료
        
        // 1. 결제 처리 시작
        payment.startProcessing();
        
        // 2. 프로세서 매핑
        Map<PaymentType, PaymentProcessor> processorMap = paymentProcessors.stream()
                .collect(Collectors.toMap(
                    PaymentProcessor::getSupportedType,
                    Function.identity()
                ));
        
        // 3. 순차적 결제 처리 (실패 시 롤백)
        List<ProcessedPayment> processedPayments = new java.util.ArrayList<>();
        
        try {
            for (PaymentMethod method : payment.getPaymentMethods()) {
                PaymentProcessor processor = processorMap.get(method.getType());
                if (processor == null) {
                    throw new PaymentFailedException("지원하지 않는 결제 수단입니다: " + method.getType());
                }
                
                PaymentProcessor.PaymentResult result = processor.process(method, payment.getId());
                if (!result.success()) {
                    throw new PaymentFailedException("결제 실패: " + result.message());
                }
                
                processedPayments.add(new ProcessedPayment(processor, method, result.externalTransactionId()));
            }
            
            // 4. 모든 결제 성공 시 Payment 상태 업데이트
            payment.complete(payment.getId());
            
        } catch (RuntimeException e) {
            // 5. 실패 시 성공한 결제들 롤백
            rollbackProcessedPayments(processedPayments, payment.getId());
            payment.fail(e.getMessage());
            throw e;
        }
    }
    
    private void rollbackProcessedPayments(List<ProcessedPayment> processedPayments, String transactionId) {
        for (ProcessedPayment processed : processedPayments) {
            try {
                processed.processor().cancel(processed.method(), transactionId, processed.externalTransactionId());
            } catch (Exception e) {
                // 롤백 실패 시 로깅 필요 시 추가
            }
        }
    }
    
    private PaymentProcessor findProcessor(PaymentType type) {
        return paymentProcessors.stream()
                .filter(p -> p.getSupportedType() == type)
                .findFirst()
                .orElseThrow(() -> new PaymentFailedException("지원하지 않는 결제 수단입니다: " + type));
    }
    
    private record ProcessedPayment(
        PaymentProcessor processor,
        PaymentMethod method,
        String externalTransactionId
    ) {}
}