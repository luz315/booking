package com.demo.booking.payment.domain.model;

import com.demo.booking.common.domain.model.Money;
import com.demo.booking.payment.domain.exception.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Payment {
    private final String id;
    private final String orderId;  // 결제를 요청한 주문 ID (booking ID, product order ID 등)
    private final String userId;
    private final List<PaymentMethod> paymentMethods;
    private final Money totalAmount;
    private PaymentStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private String failureReason;
    private String transactionId;
    
    public Payment(String orderId, String userId, List<PaymentMethod> paymentMethods, Money totalAmount) {
        this(UUID.randomUUID().toString(), orderId, userId, paymentMethods, totalAmount);
    }
    
    public Payment(String id, String orderId, String userId, List<PaymentMethod> paymentMethods, Money totalAmount) {
        if (id == null || id.trim().isEmpty()) {
            throw new PaymentInvalidInputException();
        }
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new PaymentInvalidInputException();
        }
        if (userId == null || userId.trim().isEmpty()) {
            throw new PaymentInvalidInputException();
        }
        if (paymentMethods == null || paymentMethods.isEmpty()) {
            throw new PaymentInvalidInputException();
        }
        if (totalAmount == null || totalAmount.isZero()) {
            throw new PaymentInvalidInputException();
        }
        
        validatePaymentMethods(paymentMethods);
        validateTotalAmount(paymentMethods, totalAmount);
        
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.paymentMethods = new ArrayList<>(paymentMethods);
        this.totalAmount = totalAmount;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    private void validatePaymentMethods(List<PaymentMethod> methods) {
        // 동일 결제 수단 중복 방지
        long distinctTypeCount = methods.stream()
                .map(PaymentMethod::getType)
                .distinct()
                .count();
        
        if (distinctTypeCount != methods.size()) {
            throw new PaymentInvalidInputException("동일한 결제 수단을 중복해서 사용할 수 없습니다");
        }
        
        boolean hasCard = methods.stream().anyMatch(m -> m.getType() == PaymentMethod.PaymentType.CREDIT_CARD);
        boolean hasYPay = methods.stream().anyMatch(m -> m.getType() == PaymentMethod.PaymentType.Y_PAY);
        
        if (hasCard && hasYPay) {
            throw new PaymentInvalidInputException("신용카드와 Y-Pay를 함께 사용할 수 없습니다");
        }
        
        for (PaymentMethod method : methods) {
            if (!method.isValid()) {
                throw new PaymentInvalidInputException("유효하지 않은 결제 수단입니다");
            }
        }
    }
    
    private void validateTotalAmount(List<PaymentMethod> methods, Money totalAmount) {
        Money sum = methods.stream()
                .map(PaymentMethod::getAmount)
                .reduce(Money.zero(), Money::add);
        
        if (!sum.equals(totalAmount)) {
            throw new IllegalArgumentException("결제 수단 금액 합계가 총 금액과 일치하지 않습니다");
        }
    }
    
    public void startProcessing() {
        if (this.status != PaymentStatus.PENDING) {
            throw new PaymentCannotStartProcessingException();
        }
        
        this.status = PaymentStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void complete(String transactionId) {
        if (this.status != PaymentStatus.PROCESSING) {
            throw new PaymentCannotCompleteException();
        }
        
        this.status = PaymentStatus.COMPLETED;
        this.transactionId = transactionId;
        this.completedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void fail(String reason) {
        if (this.status == PaymentStatus.COMPLETED) {
            throw new PaymentCannotFailException();
        }
        
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    
    public void timeout() {
        if (this.status == PaymentStatus.COMPLETED) {
            throw new PaymentCannotFailException();
        }
        
        this.status = PaymentStatus.TIMEOUT;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void cancel() {
        if (this.status == PaymentStatus.COMPLETED) {
            throw new PaymentCannotCancelException();
        }
        if (this.status == PaymentStatus.CANCELLED) {
            throw new PaymentAlreadyCancelledException();
        }
        
        this.status = PaymentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isCompleted() {
        return status == PaymentStatus.COMPLETED;
    }
    
    public boolean isFailed() {
        return status == PaymentStatus.FAILED;
    }
    
    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }
    
    // Getters
    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public List<PaymentMethod> getPaymentMethods() { return List.copyOf(paymentMethods); }
    public Money getTotalAmount() { return totalAmount; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public String getFailureReason() { return failureReason; }
    public String getTransactionId() { return transactionId; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", methodsCount=" + paymentMethods.size() +
                '}';
    }
}