package com.demo.booking.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "체크아웃 응답")
@Getter
@AllArgsConstructor
public class CheckoutResponse {
    
    @Schema(description = "상품 정보")
    private ProductInfo product;
    
    @Schema(description = "사용자 포인트 정보")
    private UserPointInfo userPoint;
    
    @Schema(description = "예약 정보")
    private BookingInfo booking;
    
    @Schema(description = "상품 정보")
    @Getter
    @AllArgsConstructor
    public static class ProductInfo {
        
        @Schema(description = "상품 ID", example = "1")
        private Long id;
        
        @Schema(description = "상품명", example = "디럭스 스위트룸")
        private String name;
        
        @Schema(description = "1박 가격", example = "150000")
        private BigDecimal pricePerNight;
        
        @Schema(description = "체크인 시간", example = "15:00")
        private String checkInTime;
        
        @Schema(description = "체크아웃 시간", example = "11:00") 
        private String checkOutTime;
        
        @Schema(description = "재고 상태", example = "AVAILABLE")
        private String stockStatus;
    }
    
    @Schema(description = "사용자 포인트 정보")
    @Getter
    @AllArgsConstructor
    public static class UserPointInfo {
        
        @Schema(description = "현재 포인트", example = "50000")
        private BigDecimal availablePoints;
        
        @Schema(description = "최대 사용 가능 포인트", example = "30000")
        private BigDecimal maxUsablePoints;
    }
    
    @Schema(description = "예약 정보")
    @Getter
    @AllArgsConstructor
    public static class BookingInfo {
        
        @Schema(description = "체크인 날짜", example = "2024-12-01")
        private LocalDate checkInDate;
        
        @Schema(description = "체크아웃 날짜", example = "2024-12-03")
        private LocalDate checkOutDate;
        
        @Schema(description = "숙박일수", example = "2")
        private int nights;
        
        @Schema(description = "총 금액", example = "300000")
        private BigDecimal totalAmount;
    }
    
    public static CheckoutResponse from(com.demo.booking.booking.application.usecase.CheckoutInfo checkoutInfo, LocalDate checkInDate, LocalDate checkOutDate) {
        ProductInfo productInfo = new ProductInfo(
            Long.parseLong(checkoutInfo.getProductInfo().getId()),
            checkoutInfo.getProductInfo().getName(),
            checkoutInfo.getProductInfo().getPrice().getAmount(),
            checkoutInfo.getProductInfo().getCheckInTime().toLocalTime().toString(),
            checkoutInfo.getProductInfo().getCheckOutTime().toLocalTime().toString(),
            checkoutInfo.isAvailable() ? "AVAILABLE" : "UNAVAILABLE"
        );
        
        UserPointInfo userPointInfo = new UserPointInfo(
            checkoutInfo.getUserInfo().getAvailablePoints().getAmount(),
            checkoutInfo.getUserInfo().getAvailablePoints().getAmount() // 최대 사용 가능 포인트는 현재 포인트와 동일하다고 가정
        );
        
        int nights = (int) java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BookingInfo bookingInfo = new BookingInfo(
            checkInDate,
            checkOutDate,
            nights,
            checkoutInfo.getTotalAmount().getAmount()
        );
        
        return new CheckoutResponse(productInfo, userPointInfo, bookingInfo);
    }
}