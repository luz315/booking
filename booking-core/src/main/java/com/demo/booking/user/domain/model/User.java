package com.demo.booking.user.domain.model;

import com.demo.booking.user.domain.exception.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
public class User {
    private final Long id;
    private String email;
    private String phone;
    private int point;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private User(
            Long id,
            String email,
            String phone,
            int point,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            ZonedDateTime deletedAt
    ) {
        // 비즈니스 불변 조건만 검증
        validatePoints(point);

        this.id = id;
        this.email = email;
        this.phone = phone;
        this.point = point;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
    
    public static User restore(
            Long id, String email, String phone, int point,
            ZonedDateTime createdAt, ZonedDateTime updatedAt, ZonedDateTime deletedAt
    ) {
        return User.builder()
                .id(id)
                .email(email)
                .phone(phone)
                .point(point)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }
    
    public void usePoint(int amount) {
        if (amount <= 0) {
            throw new InvalidPointValueException();
        }

        if (this.point < amount) {
            throw new InsufficientPointsException();
        }
        this.point -= amount;
        this.updatedAt = ZonedDateTime.now();
    }
    
    public void addPoint(int amount) {
        if (amount <= 0) {
            throw new InvalidPointValueException();
        }
        this.point += amount;
        this.updatedAt = ZonedDateTime.now();
    }

    // 비즈니스 불변 조건 검증
    private static void validatePoints(int point) {
        if (point < 0) {
            throw new InvalidPointValueException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}