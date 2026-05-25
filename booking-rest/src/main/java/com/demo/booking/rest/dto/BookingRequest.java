package com.demo.booking.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "예약 생성 요청")
@Getter
@NoArgsConstructor
public class BookingRequest {
    
    @Schema(description = "상품 ID", example = "1")
    @NotNull
    private Long productId;
    
    @Schema(description = "체크인 날짜", example = "2024-12-01")
    @NotNull
    private LocalDate checkInDate;
    
    @Schema(description = "체크아웃 날짜", example = "2024-12-03")
    @NotNull
    private LocalDate checkOutDate;
    
    @Schema(description = "총 결제 금액", example = "280000")
    @NotNull
    @PositiveOrZero
    private BigDecimal totalAmount;
    
    @Schema(description = "사용할 포인트", example = "20000")
    @PositiveOrZero
    private BigDecimal usePoints = BigDecimal.ZERO;
    
    @Schema(description = "멱등성 키", example = "user123_product1_20241201_abc123")
    @NotBlank
    private String idempotencyKey;
    
    public com.demo.booking.booking.application.command.CreateFlashSaleBookCommand toCommand(Long userId) {
        return new com.demo.booking.booking.application.command.CreateFlashSaleBookCommand(
            userId,
            this.productId,
            this.checkInDate,
            this.checkOutDate,
            com.demo.booking.common.domain.model.Money.of(this.totalAmount),
            this.idempotencyKey
        );
    }
}