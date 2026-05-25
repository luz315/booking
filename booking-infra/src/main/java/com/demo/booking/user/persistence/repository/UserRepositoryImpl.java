package com.demo.booking.user.persistence.repository;

import com.demo.booking.user.application.port.out.UserRepository;
import com.demo.booking.user.domain.model.User;
import com.demo.booking.user.persistence.entity.UserRecord;
import com.demo.booking.user.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final UserJpaRepository userJpaRepository;
    
    @Override
    public User save(User user) {
        UserRecord record = UserMapper.toRecord(user);
        UserRecord savedRecord = userJpaRepository.save(record);
        return UserMapper.toDomain(savedRecord);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findByIdAndDeletedAtIsNull(id)
                .map(UserMapper::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmailAndDeletedAtIsNull(email)
                .map(UserMapper::toDomain);
    }
    
    @Override
    public boolean existsById(Long id) {
        return userJpaRepository.existsByIdAndDeletedAtIsNull(id);
    }
}