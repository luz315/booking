package com.demo.booking.booking.application.usecase;

import java.time.LocalDate;

/**
 * 체크아웃 유스케이스
 * 예약 전 상품의 가격, 재고 상태 등을 확인하여 체크아웃 정보를 제공합니다.
 * 실제 예약 생성 전 단계의 정보 제공 역할을 담당합니다.
 */
public interface CheckoutUseCase {
    
    /**
     * 체크아웃 정보를 조회합니다.
     * 상품 정보, 가격, 재고 상태, 사용자 포인트 등을 확인하여 체크아웃에 필요한 정보를 제공합니다.
     * @param command 체크아웃 명령
     * @return 체크아웃 결과 (상품정보, 가격, 재고상태 포함)
     */
    CheckoutResult checkout(CheckoutCommand command);
    
    class CheckoutCommand {
        private final String userId;
        private final String productId;
        private final LocalDate checkInDate;
        private final LocalDate checkOutDate;
        private final int quantity;
        
        public CheckoutCommand(String userId, String productId, LocalDate checkInDate, LocalDate checkOutDate, int quantity) {
            this.userId = userId;
            this.productId = productId;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.quantity = quantity;
        }
        
        public String getUserId() { return userId; }
        public String getProductId() { return productId; }
        public LocalDate getCheckInDate() { return checkInDate; }
        public LocalDate getCheckOutDate() { return checkOutDate; }
        public int getQuantity() { return quantity; }
    }
    
    class CheckoutResult {
        private final CheckoutInfo checkoutInfo;
        private final String errorMessage;
        
        private CheckoutResult(CheckoutInfo checkoutInfo, String errorMessage) {
            this.checkoutInfo = checkoutInfo;
            this.errorMessage = errorMessage;
        }
        
        public static CheckoutResult success(CheckoutInfo checkoutInfo) {
            return new CheckoutResult(checkoutInfo, null);
        }
        
        public static CheckoutResult failure(String errorMessage) {
            return new CheckoutResult(null, errorMessage);
        }
        
        public boolean isSuccess() { return checkoutInfo != null; }
        public CheckoutInfo getCheckoutInfo() { return checkoutInfo; }
        public String getErrorMessage() { return errorMessage; }
    }
}