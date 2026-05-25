package com.demo.booking.user.persistence.entity;

import com.demo.booking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRecord extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "phone", nullable = false)
    private String phone;
    
    @Column(name = "point", nullable = false)
    private Integer point = 0;
    
    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;
    
    public UserRecord(String email, String phone, Integer point) {
        this.email = email;
        this.phone = phone;
        this.point = point != null ? point : 0;
    }
}