package com.demo.booking.booking.application.support;

import com.demo.booking.user.application.port.out.UserRepository;
import com.demo.booking.user.domain.model.User;
import com.demo.booking.user.domain.exception.UserNotFoundException;
import com.demo.booking.common.domain.model.Money;

import jakarta.inject.Named;

@Named
public class UserFinder {
    
    private final UserRepository userRepository;
    
    public UserFinder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * 사용자가 충분한 포인트를 보유하고 있는지 확인합니다.
     */
    public boolean hasSufficientPoints(String userId, int requiredPoints) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new UserNotFoundException());
        return user.getPoint() >= requiredPoints;
    }
}