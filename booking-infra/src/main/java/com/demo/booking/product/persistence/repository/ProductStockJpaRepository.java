package com.demo.booking.product.persistence.repository;

import com.demo.booking.product.persistence.entity.ProductStockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductStockJpaRepository extends JpaRepository<ProductStockRecord, Long> {
    
    Optional<ProductStockRecord> findByProductIdAndStayDate(Long productId, LocalDate stayDate);
    
    List<ProductStockRecord> findByProductId(Long productId);
    
    List<ProductStockRecord> findByStayDate(LocalDate stayDate);
    
    List<ProductStockRecord> findByProductIdAndStayDateBetweenAndDeletedAtIsNull(
            Long productId, 
            LocalDate startDate, 
            LocalDate endDate
    );
    
    List<ProductStockRecord> findByProductIdAndStayDateBetweenAndAvailableStockGreaterThanAndDeletedAtIsNull(
            Long productId, 
            LocalDate startDate, 
            LocalDate endDate, 
            Integer availableStock
    );
    
    Optional<ProductStockRecord> findByProductIdAndStayDateAndDeletedAtIsNull(
            Long productId, 
            LocalDate stayDate
    );
    
    List<ProductStockRecord> findByProductIdAndDeletedAtIsNull(Long productId);
    
    boolean existsByProductIdAndStayDateAndDeletedAtIsNull(
            Long productId, 
            LocalDate stayDate
    );
    
    /**
     * 비관적 락으로 재고를 조회합니다 (동시성 제어)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductStockRecord> findWithLockByProductIdAndStayDateAndDeletedAtIsNull(
            Long productId, 
            LocalDate stayDate
    );
    
}