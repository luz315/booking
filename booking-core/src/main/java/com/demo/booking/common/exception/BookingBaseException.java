package com.demo.booking.common.exception;

public class BookingBaseException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public BookingBaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BookingBaseException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}