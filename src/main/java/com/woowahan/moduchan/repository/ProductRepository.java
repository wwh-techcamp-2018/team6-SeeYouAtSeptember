package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Product findByIdAndDeletedFalse(Long id);
}
