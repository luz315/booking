package com.demo.booking.user.application.usecase;

import com.demo.booking.user.domain.model.User;

/**
 * 사용자 조회 유스케이스
 * 사용자 기본 정보를 조회하는 기능을 제공합니다.
 */
public interface GetUserUseCase {
    
    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     * @param userId 사용자 ID
     * @return 사용자 정보
     * @throws IllegalArgumentException 사용자를 찾을 수 없는 경우
     */
    User getUser(Long userId);
}