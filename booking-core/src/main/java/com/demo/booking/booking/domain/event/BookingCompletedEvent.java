package com.demo.booking.booking.domain.event;

import com.demo.booking.common.domain.model.Money;

public class BookingCompletedEvent extends BookingEvent {
    private final String userId;
    private final String productId;
    private final int quantity;
    private final Money totalAmount;
    
    public BookingCompletedEvent(String eventId, String bookingId, String userId, 
                                String productId, int quantity, Money totalAmount) {
        super(eventId, bookingId);
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public Money getTotalAmount() {
        return totalAmount;
    }
    
    @Override
    public String toString() {
        return "BookingCompletedEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", bookingId='" + getBookingId() + '\'' +
                ", userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", occurredAt=" + getOccurredAt() +
                '}';
    }
}