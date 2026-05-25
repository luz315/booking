package com.demo.booking.product.application.usecase.impl;

import com.demo.booking.product.application.port.out.ProductStockRepository;
import com.demo.booking.product.application.usecase.UpdateStockUseCase;
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
public class UpdateStockUseCaseImpl implements UpdateStockUseCase {
    
    private final ProductStockRepository productStockRepository;
    
    @Override
    public void updateStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate, int newTotalStock) {
        // 1. 일괄 조회
        List<ProductStock> stocks = productStockRepository.findByProductIdAndStayDateBetween(
            productId, checkInDate, checkOutDate.minusDays(1)
        );
        
        // 2. 비즈니스 검증
        validateStockExists(stocks, productId, checkInDate, checkOutDate);
        
        // 3. 일괄 비즈니스 로직 적용
        stocks.forEach(stock -> stock.updateTotalStock(newTotalStock));
        
        // 4. 일괄 저장
        productStockRepository.saveAll(stocks);
    }
    
    private void validateStockExists(List<ProductStock> stocks, Long productId, LocalDate checkInDate, LocalDate checkOutDate) {
        long expectedDays = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (stocks.size() != expectedDays) {
            throw new ProductStockCannotReserveException();
        }
    }
}