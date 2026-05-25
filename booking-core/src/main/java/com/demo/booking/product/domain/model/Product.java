package com.demo.booking.product.domain.model;

import com.demo.booking.common.domain.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
public class Product {
    private final Long id;
    private String name;
    private Money price;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private ProductStatus status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Product(
            Long id,
            String name,
            Money price,
            LocalTime checkInTime,
            LocalTime checkOutTime,
            ProductStatus status,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            ZonedDateTime deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
    
    public static Product restore(
            Long id, String name, Money price,
            LocalTime checkInTime, LocalTime checkOutTime, ProductStatus status,
            ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt
    ) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .status(status)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
        this.updatedAt = ZonedDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}