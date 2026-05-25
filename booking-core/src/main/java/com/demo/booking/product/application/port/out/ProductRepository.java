package com.demo.booking.product.application.port.out;

import com.demo.booking.common.domain.model.PageCriteria;
import com.demo.booking.common.domain.model.PageResult;
import com.demo.booking.product.domain.model.Product;
import java.util.Optional;

public interface ProductRepository {
    
    Product save(Product product);
    
    Optional<Product> findById(Long id);
    
    PageResult<Product> findAll(PageCriteria pageCriteria);
    
    PageResult<Product> findByNameContaining(String name, PageCriteria pageCriteria);
    
    boolean existsById(Long id);
}