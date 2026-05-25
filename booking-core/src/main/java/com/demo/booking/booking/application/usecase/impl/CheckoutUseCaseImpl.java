package com.demo.booking.booking.application.usecase.impl;

import com.demo.booking.booking.application.support.ProductFinder;
import com.demo.booking.booking.application.support.UserFinder;
import com.demo.booking.product.application.port.out.ProductRepository;
import com.demo.booking.user.application.port.out.UserRepository;
import com.demo.booking.product.domain.exception.ProductNotFoundException;
import com.demo.booking.user.domain.exception.UserNotFoundException;
import com.demo.booking.booking.application.usecase.CheckoutInfo;
import com.demo.booking.booking.application.usecase.CheckoutUseCase;
import com.demo.booking.product.domain.model.Product;
import com.demo.booking.product.domain.service.ProductStockPolicy;
import com.demo.booking.product.domain.model.ProductStock;
import java.time.LocalDate;
import com.demo.booking.user.domain.model.User;
import com.demo.booking.common.domain.model.Money;

import jakarta.inject.Named;

@Named
public class CheckoutUseCaseImpl implements CheckoutUseCase {
    
    private final ProductFinder productFinder;
    private final UserFinder userFinder;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductStockPolicy stockPolicy;
    
    public CheckoutUseCaseImpl(ProductFinder productFinder, UserFinder userFinder,
                              ProductRepository productRepository, UserRepository userRepository,
                              ProductStockPolicy stockPolicy) {
        this.productFinder = productFinder;
        this.userFinder = userFinder;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.stockPolicy = stockPolicy;
    }
    
    @Override
    public CheckoutResult checkout(CheckoutCommand command) {
        try {
            // 1. 사용자 정보 조회
            User user = userRepository.findById(Long.parseLong(command.getUserId()))
                    .orElseThrow(() -> new UserNotFoundException());
            
            // 2. 상품 정보 조회
            Product product = productRepository.findById(Long.parseLong(command.getProductId()))
                    .orElseThrow(() -> new ProductNotFoundException());
            
            // 3. 재고 가용성 확인
            boolean available = productFinder.canReserve(command.getProductId(), command.getCheckInDate(), command.getCheckOutDate(), command.getQuantity());
            
            // 4. 총 금액 계산
            Money totalAmount = product.getPrice().multiply(
                java.math.BigDecimal.valueOf(command.getQuantity())
            );
            
            // 5. 체크아웃 정보 생성
            // 재고 정보 조회 (체크인 날짜 기준)
            ProductStock stock = productFinder.findStockOrThrow(command.getProductId(), command.getCheckInDate());
            
            CheckoutInfo.ProductInfo productInfo = new CheckoutInfo.ProductInfo(
                product.getId().toString(),
                product.getName(),
                product.getPrice(),
                stock.getAvailableStock(),
                command.getCheckInDate().atTime(product.getCheckInTime()),
                command.getCheckOutDate().atTime(product.getCheckOutTime())
            );
            
            CheckoutInfo.UserInfo userInfo = new CheckoutInfo.UserInfo(
                user.getId().toString(),
                user.getEmail(),
                Money.of(user.getPoint())
            );
            
            CheckoutInfo checkoutInfo = new CheckoutInfo(
                productInfo,
                userInfo,
                command.getQuantity(),
                totalAmount,
                available
            );
            
            return CheckoutResult.success(checkoutInfo);
            
        } catch (Exception e) {
            return CheckoutResult.failure(e.getMessage());
        }
    }
}