package com.demo.booking.product.application.usecase.impl;

import com.demo.booking.product.application.port.out.ProductStockRepository;
import com.demo.booking.product.application.usecase.GetAvailableStockUseCase;
import com.demo.booking.product.domain.model.ProductStock;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.util.List;

@Named
@RequiredArgsConstructor
public class GetAvailableStockUseCaseImpl implements GetAvailableStockUseCase {
    
    private final ProductStockRepository productStockRepository;
    
    @Override
    public List<ProductStock> getAvailableStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate) {
        return productStockRepository.findByProductIdAndStayDateBetween(productId, checkInDate, checkOutDate.minusDays(1))
                .stream()
                .filter(ProductStock::isAvailable)
                .toList();
    }
}