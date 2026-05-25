package com.demo.booking.booking.application.command;

import com.demo.booking.common.domain.model.Money;
import java.time.LocalDate;

public record CreateFlashSaleBookCommand(
        Long productId,
        Long userId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Money totalAmount,
        String idempotencyKey
) {}