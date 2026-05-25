package com.demo.booking.product.persistence.entity;

import com.demo.booking.common.entity.BaseEntity;
import com.demo.booking.product.domain.model.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRecord extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    
    @Column(name = "check_in_time", nullable = false)
    private LocalTime checkInTime;
    
    @Column(name = "check_out_time", nullable = false)
    private LocalTime checkOutTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status = ProductStatus.AVAILABLE;
    
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
    
    public ProductRecord(String name, BigDecimal price, 
                        LocalTime checkInTime, LocalTime checkOutTime, 
                        ProductStatus status) {
        this.name = name;
        this.price = price;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status != null ? status : ProductStatus.AVAILABLE;
    }
}