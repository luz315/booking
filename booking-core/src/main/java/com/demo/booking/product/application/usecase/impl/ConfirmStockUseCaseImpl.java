package com.demo.booking.product.application.usecase.impl;

import com.demo.booking.product.application.port.out.ProductStockRepository;
import com.demo.booking.product.application.usecase.ConfirmStockUseCase;
import com.demo.booking.product.domain.model.ProductStock;
import com.demo.booking.product.domain.exception.ProductStockCannotConfirmException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

@Named
@RequiredArgsConstructor
@Transactional
public class ConfirmStockUseCaseImpl implements ConfirmStockUseCase {
    
    private final ProductStockRepository productStockRepository;
    
    @Override
    public void confirmStock(Long productId, LocalDate checkInDate, LocalDate checkOutDate, int quantity) {
        // 1. 일괄 조회
        List<ProductStock> stocks = productStockRepository.findByProductIdAndStayDateBetween(
            productId, checkInDate, checkOutDate.minusDays(1)
        );
        
        // 2. 비즈니스 검증
        validateStockExists(stocks, productId, checkInDate, checkOutDate);
        
        // 3. 일괄 비즈니스 로직 적용
        stocks.forEach(stock -> stock.confirmHold(quantity));
        
        // 4. 일괄 저장
        productStockRepository.saveAll(stocks);
    }
    
    private void validateStockExists(List<ProductStock> stocks, Long productId, LocalDate checkInDate, LocalDate checkOutDate) {
        long expectedDays = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (stocks.size() != expectedDays) {
            throw new ProductStockCannotConfirmException();
        }
    }
}