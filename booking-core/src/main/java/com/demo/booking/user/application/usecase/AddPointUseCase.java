package com.demo.booking.user.application.usecase;

/**
 * 사용자 포인트 적립 유스케이스
 * 사용자의 포인트를 증가시키는 기능을 제공합니다.
 */
public interface AddPointUseCase {
    
    /**
     * 사용자에게 포인트를 적립합니다.
     * @param userId 사용자 ID
     * @param amount 적립할 포인트 금액
     */
    void addPoint(Long userId, int amount);
}