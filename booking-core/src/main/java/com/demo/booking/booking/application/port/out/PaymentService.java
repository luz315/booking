package com.demo.booking.booking.application.port.out;

import com.demo.booking.common.domain.model.Money;
import java.util.List;

public interface PaymentService {
    
    PaymentResult processPayment(PaymentRequest request);
    
    PaymentStatus getPaymentStatus(String paymentId);
    
    boolean isPaymentCompleted(String paymentId);
    
    class PaymentRequest {
        private final String orderId;
        private final String userId;
        private final List<PaymentMethodRequest> paymentMethods;
        private final Money totalAmount;
        
        public PaymentRequest(String orderId, String userId, 
                            List<PaymentMethodRequest> paymentMethods, Money totalAmount) {
            this.orderId = orderId;
            this.userId = userId;
            this.paymentMethods = paymentMethods;
            this.totalAmount = totalAmount;
        }
        
        public String getOrderId() { return orderId; }
        public String getUserId() { return userId; }
        public List<PaymentMethodRequest> getPaymentMethods() { return paymentMethods; }
        public Money getTotalAmount() { return totalAmount; }
    }
    
    class PaymentMethodRequest {
        private final PaymentType type;
        private final Money amount;
        private final String details; // JSON 또는 기타 형태의 상세 정보
        
        public PaymentMethodRequest(PaymentType type, Money amount, String details) {
            this.type = type;
            this.amount = amount;
            this.details = details;
        }
        
        public PaymentType getType() { return type; }
        public Money getAmount() { return amount; }
        public String getDetails() { return details; }
    }
    
    enum PaymentType {
        CREDIT_CARD, Y_PAY, Y_POINT
    }
    
    class PaymentResult {
        private final boolean success;
        private final String paymentId;
        private final String transactionId;
        private final String errorMessage;
        
        private PaymentResult(boolean success, String paymentId, String transactionId, String errorMessage) {
            this.success = success;
            this.paymentId = paymentId;
            this.transactionId = transactionId;
            this.errorMessage = errorMessage;
        }
        
        public static PaymentResult success(String paymentId, String transactionId) {
            return new PaymentResult(true, paymentId, transactionId, null);
        }
        
        public static PaymentResult failure(String errorMessage) {
            return new PaymentResult(false, null, null, errorMessage);
        }
        
        public boolean isSuccess() { return success; }
        public String getPaymentId() { return paymentId; }
        public String getTransactionId() { return transactionId; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    enum PaymentStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
    }
}