package com.demo.booking.payment.domain.model;

import com.demo.booking.common.domain.model.Money;
import java.util.Objects;

public class YPayPayment extends PaymentMethod {
    private final String yPayAccount;
    
    public YPayPayment(Money amount, String yPayAccount) {
        super(PaymentType.Y_PAY, amount);
        if (yPayAccount == null || yPayAccount.trim().isEmpty()) {
            throw new IllegalArgumentException("Y-Pay 계정은 필수입니다");
        }
        this.yPayAccount = yPayAccount;
    }
    
    @Override
    public boolean isValid() {
        return yPayAccount != null && !yPayAccount.trim().isEmpty();
    }
    
    public String getYPayAccount() {
        return yPayAccount;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        YPayPayment that = (YPayPayment) o;
        return Objects.equals(yPayAccount, that.yPayAccount);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), yPayAccount);
    }
    
    @Override
    public String toString() {
        return "YPayPayment{" +
                "amount=" + amount +
                ", yPayAccount='" + yPayAccount + '\'' +
                '}';
    }
}