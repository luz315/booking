package com.demo.booking.product.persistence.repository;

import com.demo.booking.product.domain.model.ProductStatus;
import com.demo.booking.product.persistence.entity.ProductRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<ProductRecord, Long> {
    
    List<ProductRecord> findByStatus(ProductStatus status);
    
    Page<ProductRecord> findByNameContainingAndDeletedAtIsNull(String name, Pageable pageable);
    
    Optional<ProductRecord> findByIdAndDeletedAtIsNull(Long id);
    
    Page<ProductRecord> findByDeletedAtIsNull(Pageable pageable);
    
    List<ProductRecord> findByStatusAndDeletedAtIsNull(ProductStatus status);
    
    boolean existsByIdAndDeletedAtIsNull(Long id);
}