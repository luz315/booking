package com.demo.booking.user.application.port.out;

import com.demo.booking.user.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    
    User save(User user);
    
    Optional<User> findById(Long id);
    
    Optional<User> findByEmail(String email);
    
    boolean existsById(Long id);
}