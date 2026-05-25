package com.demo.booking.booking.application.port.out.dto;

public enum HoldStockResult {
    SUCCESS,           // 성공적으로 선점
    DUPLICATED,        // 중복 요청 (idempotencyKey)
    ALREADY_HELD       // 이미 선점함 (userId)
    // SOLD_OUT은 제거 - 예외로 처리
}