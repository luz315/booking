package com.demo.booking.product.application.usecase.impl;

import com.demo.booking.common.domain.model.Money;
import com.demo.booking.product.application.port.out.ProductRepository;
import com.demo.booking.product.application.usecase.UpdateProductUseCase;
import com.demo.booking.product.domain.model.Product;
import com.demo.booking.product.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.time.LocalTime;

@Named
@RequiredArgsConstructor
@Transactional
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {
    
    private final ProductRepository productRepository;
    
    @Override
    public Product updateProduct(Long productId, String name, Money price, LocalTime checkInTime, LocalTime checkOutTime) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());
        
        // Domain 메서드로 업데이트 로직 구현 필요
        // product.update(name, price, checkInTime, checkOutTime);
        
        return productRepository.save(product);
    }
}