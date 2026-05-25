package com.demo.booking.product.application.usecase.impl;

import com.demo.booking.product.application.port.out.ProductRepository;
import com.demo.booking.product.application.usecase.GetProductUseCase;
import com.demo.booking.product.domain.model.Product;
import com.demo.booking.product.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;

@Named
@RequiredArgsConstructor
public class GetProductUseCaseImpl implements GetProductUseCase {
    
    private final ProductRepository productRepository;
    
    @Override
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());
    }
}