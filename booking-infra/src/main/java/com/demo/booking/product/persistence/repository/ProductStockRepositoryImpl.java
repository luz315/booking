package com.demo.booking.product.persistence.repository;

import com.demo.booking.product.application.port.out.ProductStockRepository;
import com.demo.booking.product.domain.exception.ProductStockNotFoundException;
import com.demo.booking.product.domain.model.ProductStock;
import com.demo.booking.product.persistence.entity.ProductStockRecord;
import com.demo.booking.product.persistence.mapper.ProductStockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductStockRepositoryImpl implements ProductStockRepository {
    
    private final ProductStockJpaRepository productStockJpaRepository;
    
    @Override
    public ProductStock save(ProductStock stock) {
        ProductStockRecord record = ProductStockMapper.toRecord(stock);
        ProductStockRecord savedRecord = productStockJpaRepository.save(record);
        return ProductStockMapper.toDomain(savedRecord);
    }
    
    @Override
    public Optional<ProductStock> findById(Long id) {
        return productStockJpaRepository.findById(id)
                .filter(record -> record.getDeletedAt() == null)
                .map(ProductStockMapper::toDomain);
    }
    
    @Override
    public Optional<ProductStock> findByProductIdAndStayDate(Long productId, LocalDate stayDate) {
        return productStockJpaRepository.findByProductIdAndStayDateAndDeletedAtIsNull(productId, stayDate)
                .map(ProductStockMapper::toDomain);
    }
    
    @Override
    public List<ProductStock> findByProductIdAndStayDateBetween(Long productId, LocalDate startDate, LocalDate endDate) {
        return productStockJpaRepository.findByProductIdAndStayDateBetweenAndDeletedAtIsNull(productId, startDate, endDate)
                .stream()
                .map(ProductStockMapper::toDomain)
                .toList();
    }
    
    @Override
    public List<ProductStock> saveAll(List<ProductStock> stocks) {
        List<ProductStockRecord> records = stocks.stream()
                .map(ProductStockMapper::toRecord)
                .toList();
        List<ProductStockRecord> savedRecords = productStockJpaRepository.saveAll(records);
        return savedRecords.stream()
                .map(ProductStockMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void holdStock(Long productId, LocalDate stayDate, int quantity) {
        ProductStockRecord record = productStockJpaRepository
                .findWithLockByProductIdAndStayDateAndDeletedAtIsNull(productId, stayDate)
                .orElseThrow(ProductStockNotFoundException::new);

        ProductStock stock = ProductStockMapper.toDomain(record);

        stock.hold(quantity);

        record.updateAvailableStock(stock.getAvailableStock());
        record.updateTimestamp(stock.getUpdatedAt());
    }

    @Override
    @Transactional
    public void revertStock(Long productId, LocalDate stayDate, int quantity) {
        ProductStockRecord record = productStockJpaRepository
                .findWithLockByProductIdAndStayDateAndDeletedAtIsNull(productId, stayDate)
                .orElseThrow(ProductStockNotFoundException::new);

        ProductStock stock = ProductStockMapper.toDomain(record);

        stock.revertHold(quantity);

        record.updateAvailableStock(stock.getAvailableStock());
        record.updateTimestamp(stock.getUpdatedAt());
    }
}