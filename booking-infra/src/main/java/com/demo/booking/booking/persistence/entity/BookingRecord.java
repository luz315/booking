package com.demo.booking.booking.persistence.entity;

import com.demo.booking.booking.domain.model.BookingStatus;
import com.demo.booking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "bookings",
       indexes = {
           @Index(name = "idx_booking_user_id", columnList = "user_id"),
           @Index(name = "idx_booking_product_id", columnList = "product_id"),
           @Index(name = "idx_booking_idempotency_key", columnList = "idempotency_key", unique = true),
           @Index(name = "idx_booking_payment_id", columnList = "payment_id"),
           @Index(name = "idx_booking_check_in_date", columnList = "check_in_date"),
           @Index(name = "idx_booking_check_out_date", columnList = "check_out_date")
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingRecord extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
    
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;
    
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "payment_id")
    private String paymentId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;
    
    @Column(name = "flash_sale_expires_at")
    private ZonedDateTime flashSaleExpiresAt;
    
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
    
    public BookingRecord(Long id, Long userId, Long productId, LocalDate checkInDate,
                        LocalDate checkOutDate, BigDecimal totalAmount, String paymentId,
                        BookingStatus status, String idempotencyKey, ZonedDateTime flashSaleExpiresAt,
                        ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.paymentId = paymentId;
        this.status = status != null ? status : BookingStatus.PENDING;
        this.idempotencyKey = idempotencyKey;
        this.flashSaleExpiresAt = flashSaleExpiresAt;
        this.deletedAt = deletedAt;
    }
}