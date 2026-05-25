package com.demo.booking.booking.application.support;

import com.demo.booking.product.application.port.out.ProductRepository;
import com.demo.booking.product.application.port.out.ProductStockRepository;
import com.demo.booking.product.domain.exception.ProductNotFoundException;
import com.demo.booking.product.domain.exception.ProductStockNotFoundException;
import com.demo.booking.product.domain.model.Product;
import com.demo.booking.product.domain.model.ProductStock;
import com.demo.booking.product.domain.model.ProductStatus;

import jakarta.inject.Named;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Named
public class ProductFinder {
    
    private final ProductRepository productRepository;
    private final ProductStockRepository stockRepository;
    
    public ProductFinder(ProductRepository productRepository, 
                        ProductStockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }
    
    
    /**
     * 상품이 예약 가능한 상태인지 확인합니다.
     */
    public boolean isBookable(String productId) {
        Product product = productRepository.findById(Long.parseLong(productId))
                .orElseThrow(() -> new ProductNotFoundException());
        return product.getStatus() == ProductStatus.AVAILABLE;
    }
    
    /**
     * 특정 기간에 요청된 수량만큼 예약이 가능한지 확인합니다.
     * 체크인부터 체크아웃 전날까지 모든 날짜의 재고를 확인합니다.
     */
    public boolean canReserve(String productId, LocalDate checkInDate, LocalDate checkOutDate, int quantity) {
        if (!isBookable(productId)) {
            return false;
        }
        
        // 체크인부터 체크아웃 전날까지 모든 날짜의 재고 확인
        LocalDate currentDate = checkInDate;
        while (currentDate.isBefore(checkOutDate)) {
            Optional<ProductStock> stockOpt = stockRepository.findByProductIdAndStayDate(Long.parseLong(productId), currentDate);
            if (stockOpt.isEmpty() || !stockOpt.get().canHold(quantity)) {
                return false;
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return true;
    }
    
    /**
     * 특정 날짜의 재고 정보를 조회합니다.
     */
    public Optional<ProductStock> findStock(String productId, LocalDate stayDate) {
        return stockRepository.findByProductIdAndStayDate(Long.parseLong(productId), stayDate);
    }
    
    /**
     * 특정 날짜의 재고 정보를 조회하며, 없으면 예외를 발생시킵니다.
     */
    public ProductStock findStockOrThrow(String productId, LocalDate stayDate) {
        return stockRepository.findByProductIdAndStayDate(Long.parseLong(productId), stayDate)
                .orElseThrow(() -> new ProductStockNotFoundException());
    }
    
    /**
     * 기간별 재고 목록을 조회합니다.
     */
    public List<ProductStock> findStocksForPeriod(String productId, LocalDate checkInDate, LocalDate checkOutDate) {
        return stockRepository.findByProductIdAndStayDateBetween(Long.parseLong(productId), checkInDate, checkOutDate.minusDays(1));
    }
}