package com.demo.booking.booking.presentation.controller;

import com.demo.booking.booking.application.usecase.*;
import com.demo.booking.booking.application.command.CreateFlashSaleBookCommand;
import com.demo.booking.booking.application.usecase.CompleteFlashSalePaymentUseCase;
import com.demo.booking.booking.application.usecase.FailFlashSalePaymentUseCase;
import com.demo.booking.common.domain.model.Money;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Flash Sale Booking API", description = "플래시 세일 예약 관리 API")
@RequestMapping("/api/flash-sale/bookings")
@RestController
@RequiredArgsConstructor
public class FlashSaleBookingController {

    private final CreateFlashSaleBookingUseCase createFlashSaleBookingUseCase;
    private final CompleteFlashSalePaymentUseCase completeFlashSalePaymentUseCase;
    private final FailFlashSalePaymentUseCase failFlashSalePaymentUseCase;

    @Operation(
        summary = "플래시 세일 예약 생성", 
        description = "플래시 세일 상품에 대한 예약을 생성합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "예약 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "409", description = "재고 부족 또는 중복 예약"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/reserve")
    public ResponseEntity<ReserveResponse> reserve(
            @RequestBody ReserveRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey
    ) {
        Long bookingId = createFlashSaleBookingUseCase.createFlashSaleBooking(
                new CreateFlashSaleBookCommand(
                        request.productId(),
                        request.userId(),
                        request.checkInDate(),
                        request.checkOutDate(),
                        Money.of(request.totalAmount()),
                        idempotencyKey
                )
        );

        return ResponseEntity.ok(new ReserveResponse(bookingId));
    }

    @Operation(
        summary = "플래시 세일 결제 완료", 
        description = "플래시 세일 예약의 결제를 완료 처리합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "결제 완료 처리 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/payment/complete")
    public ResponseEntity<Void> completePayment(
            @RequestBody CompletePaymentRequest request
    ) {
        completeFlashSalePaymentUseCase.complete(
                request.idempotencyKey(),
                request.paymentId()
        );
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "플래시 세일 결제 실패", 
        description = "플래시 세일 예약의 결제 실패를 처리하고 재고를 복구합니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "결제 실패 처리 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/payment/fail")
    public ResponseEntity<Void> failPayment(
            @RequestBody FailPaymentRequest request
    ) {
        failFlashSalePaymentUseCase.fail(request.idempotencyKey());
        return ResponseEntity.ok().build();
    }

    // Request/Response DTOs
    public record ReserveRequest(
            Long productId,
            Long userId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Long totalAmount
    ) {}

    public record ReserveResponse(
            Long bookingId
    ) {}

    public record CompletePaymentRequest(
            String idempotencyKey,
            String paymentId
    ) {}

    public record FailPaymentRequest(
            String idempotencyKey
    ) {}
}