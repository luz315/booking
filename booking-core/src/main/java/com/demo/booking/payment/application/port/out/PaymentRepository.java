package com.demo.booking.payment.application.port.out;

import com.demo.booking.payment.domain.model.Payment;
import com.demo.booking.payment.domain.model.PaymentStatus;
import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    
    Payment save(Payment payment);
    
    Optional<Payment> findById(String id);
    
    Optional<Payment> findByOrderId(String orderId);
    
    List<Payment> findByUserId(String userId);
    
    PageResult<Payment> findByUserId(String userId, PageCriteria pageCriteria);
    
    PageResult<Payment> findAll(PageCriteria pageCriteria);
    
    void delete(Payment payment);
    
    void deleteById(String id);
    
    boolean existsById(String id);
    
    boolean existsByOrderId(String orderId);
    
    List<Payment> findTimeoutPayments(PaymentStatus status, LocalDateTime timeoutBoundary);
}