package com.demo.booking.product.application.usecase.impl;

import com.demo.booking.product.application.port.out.ProductStockRepository;
import com.demo.booking.product.application.usecase.HoldStockUseCase;
import com.demo.booking.product.domain.model.ProductStock;
import com.demo.booking.product.domain.exception.ProductStockCannotReserveException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@Named
@RequiredArgsConstructor
@Transactional
public class HoldStockUseCaseImpl implements HoldStockUseCase {
    
    private final ProductStockRepository productStockRepository;
    
    @Override
    public boolean holdStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate, int quantity) {
        // 1. 일괄 조회
        List<ProductStock> stocks = productStockRepository.findByProductIdAndStayDateBetween(
            productId, checkInDate, checkOutDate.minusDays(1)
        );
        
        // 2. 비즈니스 검증
        validateStockExists(stocks, productId, checkInDate, checkOutDate);
        
        // 3. 점유 가능성 사전 검증
        if (!canHoldAll(stocks, quantity)) {
            return false;
        }
        
        // 4. 일괄 점유 및 저장
        stocks.forEach(stock -> stock.hold(quantity));
        productStockRepository.saveAll(stocks);
        return true;
    }
    
    private void validateStockExists(List<ProductStock> stocks, Long productId, LocalDate checkInDate, LocalDate checkOutDate) {
        long expectedDays = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (stocks.size() != expectedDays) {
            throw new ProductStockCannotReserveException();
        }
    }
    
    private boolean canHoldAll(List<ProductStock> stocks, int quantity) {
        return stocks.stream().allMatch(stock -> stock.canHold(quantity));
    }
}