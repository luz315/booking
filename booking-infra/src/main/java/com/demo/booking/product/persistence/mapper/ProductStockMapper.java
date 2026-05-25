package com.demo.booking.product.persistence.mapper;

import com.demo.booking.product.domain.model.ProductStock;
import com.demo.booking.product.persistence.entity.ProductStockRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductStockMapper {
    
    public static ProductStock toDomain(ProductStockRecord record) {
        return ProductStock.restore(
                record.getId(),
                record.getProductId(),
                record.getStayDate(),
                record.getTotalStock(),
                record.getAvailableStock(),
                record.getCreatedAt(),
                record.getUpdatedAt(),
                record.getDeletedAt()
        );
    }
    
    public static ProductStockRecord toRecord(ProductStock domain) {
        return new ProductStockRecord(
                domain.getProductId(),
                domain.getStayDate(),
                domain.getTotalStock(),
                0, // reservedStock - 도메인에서 제거됨
                domain.getAvailableStock()
        );
    }
}