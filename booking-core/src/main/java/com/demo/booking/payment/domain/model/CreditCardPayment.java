package com.demo.booking.payment.domain.model;

import com.demo.booking.common.domain.model.Money;
import java.util.Objects;

public class CreditCardPayment extends PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;
    
    public CreditCardPayment(Money amount, String cardNumber, String cardHolderName) {
        super(PaymentType.CREDIT_CARD, amount);
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("카드 번호는 필수입니다");
        }
        if (cardHolderName == null || cardHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("카드 소유자 이름은 필수입니다");
        }
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }
    
    @Override
    public boolean isValid() {
        return cardNumber.length() >= 13 && cardNumber.length() <= 19 && 
               cardNumber.matches("\\d+") && 
               !cardHolderName.trim().isEmpty();
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public String getCardHolderName() {
        return cardHolderName;
    }
    
    public String getMaskedCardNumber() {
        if (cardNumber.length() < 4) return "****";
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CreditCardPayment that = (CreditCardPayment) o;
        return Objects.equals(cardNumber, that.cardNumber) && 
               Objects.equals(cardHolderName, that.cardHolderName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cardNumber, cardHolderName);
    }
    
    @Override
    public String toString() {
        return "CreditCardPayment{" +
                "amount=" + amount +
                ", cardNumber='" + getMaskedCardNumber() + '\'' +
                ", cardHolderName='" + cardHolderName + '\'' +
                '}';
    }
}