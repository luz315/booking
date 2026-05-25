package com.demo.booking.user.application.usecase.impl;

import com.demo.booking.user.application.port.out.UserRepository;
import com.demo.booking.user.application.usecase.GetUserUseCase;
import com.demo.booking.user.domain.model.User;
import com.demo.booking.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;

@Named
@RequiredArgsConstructor
public class GetUserUseCaseImpl implements GetUserUseCase {
    
    private final UserRepository userRepository;
    
    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
    }
}