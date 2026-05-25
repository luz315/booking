package com.demo.booking.user.application.usecase;

/**
 * 사용자 포인트 사용 유스케이스
 * 사용자의 포인트를 차감하는 기능을 제공합니다.
 */
public interface UsePointUseCase {
    
    /**
     * 사용자의 포인트를 사용합니다.
     * @param userId 사용자 ID
     * @param amount 사용할 포인트 금액
     * @throws IllegalArgumentException 포인트가 부족한 경우
     */
    void usePoint(Long userId, int amount);
}