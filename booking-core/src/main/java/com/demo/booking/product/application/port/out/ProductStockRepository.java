package com.demo.booking.product.application.port.out;

import com.demo.booking.product.domain.model.ProductStock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductStockRepository {
    
    ProductStock save(ProductStock stock);
    
    Optional<ProductStock> findById(Long id);
    
    Optional<ProductStock> findByProductIdAndStayDate(Long productId, LocalDate stayDate);

    List<ProductStock> findByProductIdAndStayDateBetween(Long productId, LocalDate startDate, LocalDate endDate);
    
    List<ProductStock> saveAll(List<ProductStock> stocks);

    /**
     * 비관적 락으로 재고를 선점합니다.
     * @param productId 상품 ID
     * @param stayDate 숙박 날짜
     * @param quantity 선점할 수량
     */
    void holdStock(Long productId, LocalDate stayDate, int quantity);
    
    /**
     * 만료된 예약의 재고를 원복합니다.
     * @param productId 상품 ID
     * @param stayDate 숙박 날짜
     * @param quantity 원복할 수량
     */
    void revertStock(Long productId, LocalDate stayDate, int quantity);
}