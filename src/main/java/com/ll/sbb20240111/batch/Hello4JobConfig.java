package com.ll.sbb20240111.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

/**
 * 배치 작업을 설정하는 Spring Batch Job Configuration 클래스
 * Job은 하나의 작업 단위를 나타낸다.
 *
 *  Spring Batch를 사용하여 "hello4Job"이라는 배치 작업을 설정하는 클래스입니다.
 *  "hello4Step1"은 청크 기반 작업으로, 원본 데이터를 읽어와 가공한 후 최종 결과를 화면에 출력
 */
/*
미션 5
hello4Job 생성
    hello4Step1 : 태스클릿이 아닌 청크기반작업을 수행
=> 17강, 청크방식으로 100이 출력될 때까지 난수 10개씩 추출하여 문자열로 바꾸고 10개씩 출력하는 hello4Job 배치잡 생성
 */
@Slf4j
@Configuration
public class Hello4JobConfig {
    // "hello4Job" 배치 작업을 생성하는 메서드
    @Bean
    public Job hello4Job(JobRepository jobRepository, Step hello4Step1) {
        return new JobBuilder("hello4Job", jobRepository)
                .start(hello4Step1)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @JobScope
    @Component
    public static class Hello4Step1Counter {
        private int count = 0;

        public void printCount(String where) {
            System.out.println(" count = " + ++count + " in " + where);
        }
    }

    // "hello4Step1" 청크 기반 작업을 설정하는 메서드
    @JobScope
    @Bean
    public Step hello4Step1(
            JobRepository jobRepository,
            Hello4Step1Reader hello4Step1Reader,
            Hello4Step1Processor hello4Step1Processor,
            Hello4Step1Writer hello4Step1Writer,
            PlatformTransactionManager platformTransactionManager
    ) {
        return new StepBuilder("hello4Step1Tasklet", jobRepository)
                .<Integer, String>chunk(10, platformTransactionManager)
                .reader(hello4Step1Reader)
                .processor(hello4Step1Processor)
                .writer(hello4Step1Writer)
                .build();
    }

    // 원본 데이터 읽기
    // 원본 데이터를 읽어오는 ItemReader 구현 클래스
    @StepScope
    @Component
    @RequiredArgsConstructor
    public static class Hello4Step1Reader implements ItemReader<Integer> {
        private final Hello4Step1Counter hello4Step1Counter;

        @Override
        public Integer read() {
            hello4Step1Counter.printCount("Reader");

            // 0에서 500 사이의 난수를 생성하여 반환
            int no = (int) (Math.random() * 200);

            // 특정 조건에 도달하면(null인 경우) 읽기를 중단
            if (no==100) return null;

            return no;
        }
    }

    // 원본 데이터 가공해서 파생 데이터 생성
    // 원본 데이터를 가공하여 새로운 형태로 변환하는 ItemProcessor 구현 클래스
    // EX : 50 -> "no. 50"
    @StepScope
    @Component
    @RequiredArgsConstructor
    public static class Hello4Step1Processor implements ItemProcessor<Integer, String> {
        private final Hello4Step1Counter hello4Step1Counter;

        @Override
        public String process(Integer item) {
            hello4Step1Counter.printCount("Processor");

            // 원본 데이터를 가공하여 문자열 반환
            return "no. " + item;
        }
    }

    // 파생 데이터를 화면에 출력
    // 최종 처리 결과를 화면에 출력하는 ItemWriter 구현 클래스
    @StepScope
    @Component
    @RequiredArgsConstructor
    public static class Hello4Step1Writer implements ItemWriter<String> {
        private final Hello4Step1Counter hello4Step1Counter;

        @Override
        public void write(Chunk<? extends String> chunk) {
            // 처리된 항목들을 화면에 출력
            List<String> items = (List<String>) chunk.getItems();

            for (String item : items) {
                hello4Step1Counter.printCount("Writer, item = " + item);
            }
        }
    }
}
