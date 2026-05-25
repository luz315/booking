package com.demo.booking.payment.domain.model;

import com.demo.booking.common.domain.model.Money;
import java.util.Objects;

public class YPointPayment extends PaymentMethod {
    private final String userId;
    
    public YPointPayment(Money amount, String userId) {
        super(PaymentType.Y_POINT, amount);
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다");
        }
        this.userId = userId;
    }
    
    @Override
    public boolean isValid() {
        return userId != null && !userId.trim().isEmpty();
    }
    
    public String getUserId() {
        return userId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YPointPayment that = (YPointPayment) o;
        return Objects.equals(userId, that.userId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId);
    }
    
    @Override
    public String toString() {
        return "YPointPayment{" +
                "amount=" + amount +
                ", userId='" + userId + '\'' +
                '}';
    }
}