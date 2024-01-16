package com.ll.sbb20240111.global.initData;

import com.ll.sbb20240111.domain.product.product.entity.Product;
import com.ll.sbb20240111.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class NotProd {
    @Autowired
    @Lazy
    private NotProd self;
    private final ProductService productService;

    @Bean
    ApplicationRunner initNotProd() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        // 상품 중 처음 만들어진 50개의 생성날짜를 수정(24시간을 뺌)
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Product product = productService.create("product " + i);

            product.setCreateDate(LocalDateTime.now().minusDays(1));
        });

        IntStream.rangeClosed(51, 100).forEach(i -> productService.create("product " + i));
    }
}
