package com.demo.booking.user.application.usecase.impl;

import com.demo.booking.user.application.port.out.UserRepository;
import com.demo.booking.user.application.usecase.GetPointUseCase;
import com.demo.booking.user.domain.model.User;
import com.demo.booking.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;

@Named
@RequiredArgsConstructor
public class GetPointUseCaseImpl implements GetPointUseCase {
    
    private final UserRepository userRepository;
    
    @Override
    public int getPoint(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        
        return user.getPoint();
    }
}