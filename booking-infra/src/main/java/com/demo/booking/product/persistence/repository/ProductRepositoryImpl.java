package com.demo.booking.product.persistence.repository;

import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;
import com.demo.booking.common.util.PageableUtils;
import com.demo.booking.product.application.port.out.ProductRepository;
import com.demo.booking.product.domain.model.Product;
import com.demo.booking.product.persistence.entity.ProductRecord;
import com.demo.booking.product.persistence.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductJpaRepository productJpaRepository;
    
    @Override
    public Product save(Product product) {
        ProductRecord record = ProductMapper.toRecord(product);
        ProductRecord savedRecord = productJpaRepository.save(record);
        return ProductMapper.toDomain(savedRecord);
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findByIdAndDeletedAtIsNull(id)
                .map(ProductMapper::toDomain);
    }
    
    @Override
    public PageResult<Product> findAll(PageCriteria pageCriteria) {
        Pageable pageable = PageableUtils.createPageable(pageCriteria);
        Page<ProductRecord> page = productJpaRepository.findByDeletedAtIsNull(pageable);
        
        List<Product> products = page.getContent().stream()
                .map(ProductMapper::toDomain)
                .toList();
                
        return new PageResult<>(
                products,
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
    
    @Override
    public PageResult<Product> findByNameContaining(String name, PageCriteria pageCriteria) {
        Pageable pageable = PageableUtils.createPageable(pageCriteria);
        Page<ProductRecord> page = productJpaRepository.findByNameContainingAndDeletedAtIsNull(name, pageable);
        
        List<Product> products = page.getContent().stream()
                .map(ProductMapper::toDomain)
                .toList();
                
        return new PageResult<>(
                products,
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
    
    @Override
    public boolean existsById(Long id) {
        return productJpaRepository.existsByIdAndDeletedAtIsNull(id);
    }
}