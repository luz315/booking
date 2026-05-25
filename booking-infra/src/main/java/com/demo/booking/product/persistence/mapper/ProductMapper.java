package com.demo.booking.product.persistence.mapper;

import com.demo.booking.common.domain.model.Money;
import com.demo.booking.product.domain.model.Product;
import com.demo.booking.product.persistence.entity.ProductRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductMapper {
    
    public static Product toDomain(ProductRecord record) {
        return Product.restore(
                record.getId(),
                record.getName(),
                Money.of(record.getPrice()),
                record.getCheckInTime(),
                record.getCheckOutTime(),
                record.getStatus(),
                record.getCreatedAt(),
                record.getUpdatedAt(),
                record.getDeletedAt()
        );
    }
    
    public static ProductRecord toRecord(Product domain) {
        return new ProductRecord(
                domain.getName(),
                domain.getPrice().getAmount(),
                domain.getCheckInTime(),
                domain.getCheckOutTime(),
                domain.getStatus()
        );
    }
}