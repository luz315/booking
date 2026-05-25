package com.demo.booking.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Schema(description = "예약 생성 응답")
@Getter
@AllArgsConstructor
public class BookingResponse {
    
    @Schema(description = "예약 ID", example = "12345")
    private Long bookingId;
    
    @Schema(description = "예약 상태", example = "CONFIRMED")
    private String status;
    
    @Schema(description = "상품 ID", example = "1")
    private Long productId;
    
    @Schema(description = "체크인 날짜", example = "2024-12-01")
    private LocalDate checkInDate;
    
    @Schema(description = "체크아웃 날짜", example = "2024-12-03")
    private LocalDate checkOutDate;
    
    @Schema(description = "총 결제 금액", example = "280000")
    private BigDecimal totalAmount;
    
    @Schema(description = "사용된 포인트", example = "20000")
    private BigDecimal usedPoints;
    
    @Schema(description = "예약 생성 시간")
    private ZonedDateTime createdAt;
    
    @Schema(description = "멱등성 키", example = "user123_product1_20241201_abc123")
    private String idempotencyKey;
}