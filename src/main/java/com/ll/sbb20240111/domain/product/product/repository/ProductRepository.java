package com.ll.sbb20240111.domain.product.product.repository;

import com.ll.sbb20240111.domain.product.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
