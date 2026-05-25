package com.demo.booking.user.domain.exception;

import com.demo.booking.common.exception.BookingBaseException;

public class UserNotFoundException extends BookingBaseException {
    
    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다");
    }
}