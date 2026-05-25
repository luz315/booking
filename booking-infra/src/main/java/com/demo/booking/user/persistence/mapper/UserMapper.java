package com.demo.booking.user.persistence.mapper;

import com.demo.booking.user.domain.model.User;
import com.demo.booking.user.persistence.entity.UserRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    
    public static User toDomain(UserRecord record) {
        return User.restore(
                record.getId(),
                record.getEmail(),
                record.getPhone(),
                record.getPoint(),
                record.getCreatedAt(),
                record.getUpdatedAt(),
                record.getDeletedAt()
        );
    }
    
    public static UserRecord toRecord(User domain) {
        return new UserRecord(
                domain.getEmail(),
                domain.getPhone(),
                domain.getPoint()
        );
    }
}