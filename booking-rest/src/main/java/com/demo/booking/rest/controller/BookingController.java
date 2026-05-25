package com.demo.booking.rest.controller;

import com.demo.booking.booking.application.command.CreateFlashSaleBookCommand;
import com.demo.booking.booking.application.usecase.CheckoutUseCase;
import com.demo.booking.booking.application.usecase.CreateFlashSaleBookingUseCase;
import com.demo.booking.common.domain.model.Money;
import com.demo.booking.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Tag(name = "Checkout API", description = "체크아웃 및 예약 관리 API")
@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final CheckoutUseCase checkoutUseCase;
    private final CreateFlashSaleBookingUseCase createFlashSaleBookingUseCase;

    @Operation(
        summary = "체크아웃 정보 조회", 
        description = "예약 전 상품 정보, 가격, 재고 상태, 사용자 포인트 등을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "체크아웃 정보 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
        @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public ResponseEntity<CheckoutResponse> getCheckout(
            @Parameter(description = "사용자 ID", required = true, example = "123")
            @RequestParam Long userId,
            @Parameter(description = "상품 ID", required = true, example = "1")
            @RequestParam Long productId,
            @Parameter(description = "체크인 날짜", required = true, example = "2024-12-01")
            @RequestParam String checkInDate,
            @Parameter(description = "체크아웃 날짜", required = true, example = "2024-12-03") 
            @RequestParam String checkOutDate,
            @Parameter(description = "수량", example = "1")
            @RequestParam(defaultValue = "1") int quantity
    ) {
        try {
            CheckoutRequest request = CheckoutRequest.from(userId, productId, checkInDate, checkOutDate, quantity);
            
            CheckoutUseCase.CheckoutCommand command = new CheckoutUseCase.CheckoutCommand(
                String.valueOf(userId),
                String.valueOf(request.getProductId()),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                request.getQuantity()
            );
            
            CheckoutUseCase.CheckoutResult result = checkoutUseCase.checkout(command);
            
            if (!result.isSuccess()) {
                return ResponseEntity.badRequest().build();
            }

            CheckoutResponse response = CheckoutResponse.from(
                result.getCheckoutInfo(),
                request.getCheckInDate(),
                request.getCheckOutDate()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("체크아웃 조회 실패: userId={}, productId={}", userId, productId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
        summary = "예약 생성",
        description = "결제를 진행하고 최종 예약을 생성합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "예약 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "재고 부족 또는 중복 예약"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Parameter(description = "사용자 ID", required = true, example = "123")
            @RequestParam Long userId,
            @Parameter(description = "예약 요청 데이터")
            @Valid @RequestBody BookingRequest request
    ) {
        try {
            CreateFlashSaleBookCommand command = request.toCommand(userId);
            Long bookingId = createFlashSaleBookingUseCase.createFlashSaleBooking(command);
            
            BookingResponse response = new BookingResponse(
                bookingId,
                "CONFIRMED",
                request.getProductId(),
                request.getCheckInDate(),
                request.getCheckOutDate(),
                request.getTotalAmount(),
                request.getUsePoints(),
                java.time.ZonedDateTime.now(),
                request.getIdempotencyKey()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("예약 생성 실패: userId={}, productId={}", userId, request.getProductId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}