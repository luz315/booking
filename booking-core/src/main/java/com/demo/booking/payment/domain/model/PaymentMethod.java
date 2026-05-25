package com.demo.booking.payment.domain.model;

import com.demo.booking.common.domain.model.Money;
import java.util.Objects;

public abstract class PaymentMethod {
    protected final PaymentType type;
    protected final Money amount;
    
    protected PaymentMethod(PaymentType type, Money amount) {
        if (type == null) {
            throw new IllegalArgumentException("결제 유형은 필수입니다");
        }
        if (amount == null || amount.isZero()) {
            throw new IllegalArgumentException("결제 금액은 0보다 커야 합니다");
        }
        this.type = type;
        this.amount = amount;
    }
    
    public abstract boolean isValid();
    
    public PaymentType getType() {
        return type;
    }
    
    public Money getAmount() {
        return amount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return type == that.type && Objects.equals(amount, that.amount);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, amount);
    }
    
    public enum PaymentType {
        CREDIT_CARD, Y_PAY, Y_POINT
    }
}