package com.demo.booking.product.application.usecase;

import com.demo.booking.common.domain.model.Money;
import com.demo.booking.product.domain.model.Product;

import java.time.LocalTime;

public interface UpdateProductUseCase {
    Product updateProduct(Long productId, String name, Money price, LocalTime checkInTime, LocalTime checkOutTime);
}