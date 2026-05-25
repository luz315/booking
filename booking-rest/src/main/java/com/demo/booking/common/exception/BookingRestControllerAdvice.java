package com.demo.booking.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
// import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class BookingRestControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(BookingRestControllerAdvice.class);

    @ExceptionHandler(BookingBaseException.class)
    public ProblemDetail handleBookingBaseException(HttpServletRequest request, BookingBaseException e) {
        log.warn(
            "handleBookingBaseException message: {}, URL: {}, Method: {}",
            e.getMessage(), request.getRequestURL(), request.getMethod(),
            e
        );

        ProblemDetail problemDetail = createProblemDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            e.getMessage(),
            request
        );
        problemDetail.setProperty("errorCode", e.getErrorCode().getCode());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.warn(
            "handleValidationException message: {}, URL: {}, Method: {}",
            e.getMessage(), request.getRequestURL(), request.getMethod(),
            e
        );

        ProblemDetail problemDetail = createProblemDetail(
            HttpStatus.BAD_REQUEST,
            "입력값 검증에 실패했습니다",
            request
        );
        
        var errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                    error.getField(),
                    error.getDefaultMessage() != null ? error.getDefaultMessage() : "유효하지 않은 값입니다"
                ))
                .toList();

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        log.warn(
            "handleIllegalArgumentException message: {}, URL: {}, Method: {}",
            e.getMessage(), request.getRequestURL(), request.getMethod(),
            e
        );

        return createProblemDetail(
            HttpStatus.BAD_REQUEST,
            e.getMessage() != null ? e.getMessage() : "잘못된 요청입니다",
            request
        );
    }

    // Spring Security 의존성이 없어서 주석처리
    // @ExceptionHandler(AccessDeniedException.class)
    // public ProblemDetail handleAccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
    //     return createProblemDetail(
    //         HttpStatus.FORBIDDEN,
    //         e.getMessage() != null ? e.getMessage() : "FORBIDDEN",
    //         request
    //     );
    // }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(HttpServletRequest request, Exception e) {
        log.error("handleGenericException URL: {}, Method: {}", request.getRequestURL(), request.getMethod(), e);

        return createProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            e.getMessage() != null ? e.getMessage() : "서버 내부 오류가 발생했습니다",
            request
        );
    }

    private ProblemDetail createProblemDetail(
            HttpStatus status,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return problemDetail;
    }

    private record ValidationError(String field, String message) {}
}