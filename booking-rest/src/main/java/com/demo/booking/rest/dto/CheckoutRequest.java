package com.demo.booking.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "체크아웃 요청")
@Getter
@NoArgsConstructor
public class CheckoutRequest {
    
    @Schema(description = "상품 ID", example = "1")
    @NotNull
    @Positive
    private Long productId;
    
    @Schema(description = "체크인 날짜", example = "2024-12-01")
    @NotNull
    private LocalDate checkInDate;
    
    @Schema(description = "체크아웃 날짜", example = "2024-12-03")
    @NotNull
    private LocalDate checkOutDate;
    
    @Schema(description = "수량", example = "1")
    @Positive
    private int quantity = 1;
    
    public CheckoutRequest(Long productId, LocalDate checkInDate, LocalDate checkOutDate, int quantity) {
        this.productId = productId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.quantity = quantity;
    }
    
    public static CheckoutRequest from(Long userId, Long productId, String checkInDate, String checkOutDate, int quantity) {
        return new CheckoutRequest(
            productId,
            LocalDate.parse(checkInDate),
            LocalDate.parse(checkOutDate),
            quantity
        );
    }
}