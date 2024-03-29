package com.ll.sbb20240111.domain.product.product.service;

import com.ll.sbb20240111.domain.product.product.entity.Product;
import com.ll.sbb20240111.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product create(String name) {
        Product product = Product
                .builder()
                .name(name)
                .build();

        return productRepository.save(product);
    }
}
