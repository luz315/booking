package com.demo.booking.user.persistence.repository;

import com.demo.booking.user.persistence.entity.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserRecord, Long> {
    
    Optional<UserRecord> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<UserRecord> findByIdAndDeletedAtIsNull(Long id);
    
    Optional<UserRecord> findByEmailAndDeletedAtIsNull(String email);
    
    boolean existsByIdAndDeletedAtIsNull(Long id);
    
    boolean existsByEmailAndDeletedAtIsNull(String email);
}