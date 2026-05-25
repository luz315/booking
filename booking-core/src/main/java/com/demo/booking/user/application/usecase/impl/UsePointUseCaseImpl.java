package com.demo.booking.user.application.usecase.impl;

import com.demo.booking.user.application.port.out.UserRepository;
import com.demo.booking.user.application.usecase.UsePointUseCase;
import com.demo.booking.user.domain.model.User;
import com.demo.booking.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

@Named
@RequiredArgsConstructor
@Transactional
public class UsePointUseCaseImpl implements UsePointUseCase {
    
    private final UserRepository userRepository;
    
    @Override
    public void usePoint(Long userId, int point) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        
        user.usePoint(point);
        
        userRepository.save(user);
    }
}