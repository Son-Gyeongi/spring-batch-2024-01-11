package com.ll.sbb20240111.batch;

import com.ll.sbb20240111.domain.product.product.entity.Product;
import com.ll.sbb20240111.domain.product.product.entity.ProductLog;
import com.ll.sbb20240111.domain.product.product.repository.ProductLogRepository;
import com.ll.sbb20240111.domain.product.product.repository.ProductRepository;
import com.ll.sbb20240111.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

/**
 * Spring Batch를 사용하여 "makeProductLogJob"이라는 배치 작업을 설정하는 클래스입니다.
 * "makeProductLogStep1"은 청크 기반 작업으로,
 * Product 엔터티를 읽어와 ProductLog 엔터티로 가공하여 저장하는 작업을 수행
 */
/*
미션 6
잡 파라미터를 활용해서
생성날짜가 2024-01-15 인 상품만 로그가 생기도록
-> 25강, 잡 파라미터를 이용해서 로깅의 구간(날짜 기준)을 설정
-> step1Reader()에 날짜 파라미터만 추가해서 ProductRepository에서 해당 날짜 사이의 데이터만 불러준다.
 */
@Configuration
@RequiredArgsConstructor
public class MakeProductLogJobConfig {
    private final int CHUNK_SIZE = 20; // 청크 단위에 맞춰서 Reader에 적용해주면 한번에 불러올 수 있다.
    private final ProductRepository productRepository;
    private final ProductLogRepository productLogRepository;

    // "makeProductLogJob" 배치 작업을 생성하는 메서드
    // "makeProductLogStep1" 단일 스텝으로 구성되어 있고, 실행 ID를 증가시키도록 설정
    @Bean
    public Job makeProductLogJob(JobRepository jobRepository, Step makeProductLogStep1) {
        return new JobBuilder("makeProductLogStep1", jobRepository)
                .start(makeProductLogStep1)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    // "makeProductLogStep1" 청크 기반 작업을 설정하는 메서드
    // Product 엔터티를 읽어와서 Processor를 통해 ProductLog 엔터티로 가공하고, 이를 Writer를 통해 저장
    @JobScope
    @Bean
    public Step makeProductLogStep1(
            JobRepository jobRepository,
            ItemReader<Product> step1Reader,
            ItemProcessor<Product, ProductLog> step1Processor,
            ItemWriter<ProductLog> step1Writer,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("makeProductLogStep1", jobRepository)
                .<Product, ProductLog>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(step1Reader)
                .processor(step1Processor)
                .writer(step1Writer)
                .build();
    }

    // "step1Reader"를 설정하는 메서드
    /*
    Product 엔터티를 읽어오는 메서드
    Spring Data JPA의 RepositoryItemReader를 사용하여 페이지 단위로 데이터를 읽어옴
     */
    @StepScope
    @Bean
    public ItemReader<Product> step1Reader(
            @Value("#{jobParameters['startDate']}") String _startDate,
            @Value("#{jobParameters['endDate']}") String _endDate
    ) {
        LocalDateTime startDate = Ut.date.parse(_startDate);
        LocalDateTime endDate = Ut.date.parse(_endDate);

        return new RepositoryItemReaderBuilder<Product>()
                .name("step1Reader")
                .repository(productRepository)
                .methodName("findByCreateDateBetween")
                .pageSize(CHUNK_SIZE) // pageSize 정해주지 않으면 기본이 10이다.
                .arguments(Arrays.asList(startDate, endDate))
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    // "step1Processor"를 설정하는 메서드
    // Product를 받아와서 ProductLog로 가공하는 메서드로, 람다 표현식으로 간단하게 표현
    @StepScope
    @Bean
    public ItemProcessor<Product, ProductLog> step1Processor() {
        return product -> ProductLog
                .builder()
                .product(product)
                .name(product.getName())
                .build();
    }

    // "step1Writer"를 설정하는 메서드
    // ProductLog 엔터티를 저장하는 메서드로, 받아온 아이템들을 productLogRepository를 통해 저장
    @StepScope
    @Bean
    public ItemWriter<ProductLog> step1Writer() {
        return items -> items.forEach(item -> {
            // 잡이 실패한 후에 다시 실행이 되어도 이미 저장된 데이터는 저장 되지 않고
            // 새로운 데이터만 저장이 되게 만들었다.
            if (!productLogRepository.existsByProduct(item.getProduct()))
                productLogRepository.save(item);
            // "productLogRepository"를 이용하여 ProductLog 엔터티를 저장
        });
    }
}
