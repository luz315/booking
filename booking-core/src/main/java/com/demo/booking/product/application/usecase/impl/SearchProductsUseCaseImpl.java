package com.demo.booking.product.application.usecase.impl;

import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;
import com.demo.booking.product.application.port.out.ProductRepository;
import com.demo.booking.product.application.usecase.SearchProductsUseCase;
import com.demo.booking.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import jakarta.inject.Named;

@Named
@RequiredArgsConstructor
public class SearchProductsUseCaseImpl implements SearchProductsUseCase {
    
    private final ProductRepository productRepository;
    
    @Override
    public PageResult<Product> searchProducts(String name, PageCriteria pageCriteria) {
        if (name == null || name.trim().isEmpty()) {
            return listProducts(pageCriteria);
        }
        return productRepository.findByNameContaining(name, pageCriteria);
    }
    
    @Override
    public PageResult<Product> listProducts(PageCriteria pageCriteria) {
        return productRepository.findAll(pageCriteria);
    }
}