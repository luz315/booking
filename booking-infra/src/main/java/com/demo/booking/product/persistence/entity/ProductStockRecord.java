package com.demo.booking.product.persistence.entity;

import com.demo.booking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "product_stocks", 
       indexes = {
           @Index(name = "idx_product_stock_product_stay_date", 
                  columnList = "product_id, stay_date", unique = true),
           @Index(name = "idx_product_stock_stay_date", 
                  columnList = "stay_date")
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductStockRecord extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "stay_date", nullable = false)
    private LocalDate stayDate;
    
    @Column(name = "total_stock", nullable = false)
    private Integer totalStock;
    
    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock = 0;
    
    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;
    
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
    
    public ProductStockRecord(Long productId, LocalDate stayDate, 
                             Integer totalStock, Integer reservedStock, 
                             Integer availableStock) {
        this.productId = productId;
        this.stayDate = stayDate;
        this.totalStock = totalStock != null ? totalStock : 0;
        this.reservedStock = reservedStock != null ? reservedStock : 0;
        this.availableStock = availableStock != null ? availableStock : 0;
    }
    
    public void updateAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
    
    public void updateTimestamp(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
    }
}