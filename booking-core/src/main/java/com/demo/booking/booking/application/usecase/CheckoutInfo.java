package com.demo.booking.booking.application.usecase;

import com.demo.booking.common.domain.model.Money;
import java.time.LocalDateTime;

public class CheckoutInfo {
    private final ProductInfo productInfo;
    private final UserInfo userInfo;
    private final int requestedQuantity;
    private final Money totalAmount;
    private final boolean available;
    
    public CheckoutInfo(ProductInfo productInfo, UserInfo userInfo, 
                       int requestedQuantity, Money totalAmount, boolean available) {
        this.productInfo = productInfo;
        this.userInfo = userInfo;
        this.requestedQuantity = requestedQuantity;
        this.totalAmount = totalAmount;
        this.available = available;
    }
    
    public ProductInfo getProductInfo() { return productInfo; }
    public UserInfo getUserInfo() { return userInfo; }
    public int getRequestedQuantity() { return requestedQuantity; }
    public Money getTotalAmount() { return totalAmount; }
    public boolean isAvailable() { return available; }
    
    public static class ProductInfo {
        private final String id;
        private final String name;
        private final Money price;
        private final int availableQuantity;
        private final LocalDateTime checkInTime;
        private final LocalDateTime checkOutTime;
        
        public ProductInfo(String id, String name, Money price, int availableQuantity,
                          LocalDateTime checkInTime, LocalDateTime checkOutTime) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.availableQuantity = availableQuantity;
            this.checkInTime = checkInTime;
            this.checkOutTime = checkOutTime;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public Money getPrice() { return price; }
        public int getAvailableQuantity() { return availableQuantity; }
        public LocalDateTime getCheckInTime() { return checkInTime; }
        public LocalDateTime getCheckOutTime() { return checkOutTime; }
    }
    
    public static class UserInfo {
        private final String id;
        private final String name;
        private final Money availablePoints;
        
        public UserInfo(String id, String name, Money availablePoints) {
            this.id = id;
            this.name = name;
            this.availablePoints = availablePoints;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public Money getAvailablePoints() { return availablePoints; }
    }
    
    @Override
    public String toString() {
        return "CheckoutInfo{" +
                "productInfo=" + productInfo.name +
                ", userInfo=" + userInfo.name +
                ", requestedQuantity=" + requestedQuantity +
                ", totalAmount=" + totalAmount +
                ", available=" + available +
                '}';
    }
}