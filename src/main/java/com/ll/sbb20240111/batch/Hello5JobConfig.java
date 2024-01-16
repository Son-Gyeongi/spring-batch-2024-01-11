package com.ll.sbb20240111.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * 배치 작업을 설정하는 Spring Batch Job Configuration 클래스
 * Job은 하나의 작업 단위를 나타낸다.
 */
/*
21강, 람다를 이용해서 Reader, Processor, Writer 를 쉽게 구현
-> Hello4JobConfig랑 같다.
 */
@Slf4j
@Configuration
public class Hello5JobConfig {
    // "hello5Job" 배치 작업을 생성하는 메서드
    @Bean
    public Job hello5Job(JobRepository jobRepository, Step hello5Step1) {
        return new JobBuilder("hello5Job", jobRepository)
                .start(hello5Step1)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    // "hello5Step1" 청크 기반 작업을 설정하는 메서드
    @JobScope
    @Bean
    public Step hello5Step1(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("hello5Step1Tasklet", jobRepository)
                .<Integer, String>chunk(10, platformTransactionManager)
                .reader(() -> {
                    // 0에서 100 사이의 난수를 생성하여 반환
                    Integer no = (int) (Math.random() * 100);

                    // 특정 조건에 도달하면(null인 경우) 읽기를 중단
                    if (no == 50) return null;

                    return no;
                })
                .processor(
                        // 아이템을 가공하여 새로운 형태로 반환하는 람다 표현식
                        item -> "No. " + item
                )
                .writer(
                        // 청크의 아이템을 화면에 출력하는 람다 표현식
                        chunk -> {
                            List<? extends String> items = chunk.getItems();

                            for (String item : items) {
                                System.out.println("item = " + item);
                            }
                        }
                )
                .build();
    }
}
