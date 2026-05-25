package com.demo.booking.product.application.usecase;

import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;
import com.demo.booking.product.domain.model.Product;

public interface SearchProductsUseCase {
    PageResult<Product> searchProducts(String name, PageCriteria pageCriteria);
    PageResult<Product> listProducts(PageCriteria pageCriteria);
}