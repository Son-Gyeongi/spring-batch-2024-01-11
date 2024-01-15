package com.ll.sbb20240111.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 배치 작업을 설정하는 Spring Batch Job Configuration 클래스
 * Job은 하나의 작업 단위를 나타낸다.
 */
/*
미션 4
hello3Job 생성
    hello3Step1, hello3Step2 : 먼저 병렬로 실행
    hello3Step3 : 나중에 실행
테스트케이스로 실행
 */
@Slf4j
@Configuration
public class Hello3JobConfig {
    // Job 생성 메서드
    @Bean
    public Job hello3Job(JobRepository jobRepository, Step hello3Step1, Step hello3Step2) { // 잡이 한개 있으면 이름 바꿔도 상관없지만 알아보기 쉽게 hello3Step1로 바꾸었다.
        // JobBuilder를 사용하여 'hello3Job'이라는 이름의 Job을 생성하고, simpleStep1 스텝을 시작으로 설정
        return new JobBuilder("hello3Job", jobRepository)
                .start(hello3Step1)
                .next(hello3Step2)
                .incrementer(new RunIdIncrementer()) // Job 파라미터를 위한 Incrementer 설정, 이거는 자동으로 실행될 때 작동한다.
                .build();
    }

    // Step 생성 메서드
    @JobScope
    @Bean
    public Step hello3Step1(JobRepository jobRepository, Tasklet hello3Step1Tasklet,
                           PlatformTransactionManager platformTransactionManager) { // hello3Step1Tasklet1는 hello3Step1Tasklet1 이다.
        // StepBuilder를 사용하여 'hello3Step1Tasklet1'이라는 이름의 Step을 생성하고, Tasklet과 TransactionManager를 설정
        return new StepBuilder("hello3Step1Tasklet", jobRepository)
                .tasklet(hello3Step1Tasklet, platformTransactionManager)
                .build();
    }

    // Tasklet 생성 메서드
    @StepScope
    @Bean
    public Tasklet hello3Step1Tasklet() {
        // Tasklet을 생성하고, execute 메서드에서 "Hello World"를 로깅하고 콘솔에 출력한 후 RepeatStatus.FINISHED를 반환
        return ((contribution, chunkContext) -> {
            System.out.println("Hello World 2-1");
            return RepeatStatus.FINISHED;
        });
    }

    // Step 생성 메서드
    @JobScope
    @Bean
    public Step hello3Step2(JobRepository jobRepository, Tasklet hello3Step2Tasklet,
                            PlatformTransactionManager platformTransactionManager) { // hello3Step2Tasklet1는 hello3Step2Tasklet1 이다.
        // StepBuilder를 사용하여 'hello3Step2Tasklet1'이라는 이름의 Step을 생성하고, Tasklet과 TransactionManager를 설정
        return new StepBuilder("hello3Step2Tasklet", jobRepository)
                .tasklet(hello3Step2Tasklet, platformTransactionManager)
                .build();
    }

    // Tasklet 생성 메서드
    @StepScope
    @Bean
    public Tasklet hello3Step2Tasklet() {
        // Tasklet을 생성하고, execute 메서드에서 "Hello World"를 로깅하고 콘솔에 출력한 후 RepeatStatus.FINISHED를 반환
        return ((contribution, chunkContext) -> {
            System.out.println("Hello World 2-2");
            return RepeatStatus.FINISHED;
        });
    }

    @JobScope
    @Bean
    public Step hello3Step3(JobRepository jobRepository, Tasklet hello3Step3Tasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("hello3Step3Tasklet", jobRepository)
                .tasklet(hello3Step3Tasklet, platformTransactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet hello3Step3Tasklet() {
        return ((contribution, chunkContext) -> {
            System.out.println("Hello World 3-3");
            return RepeatStatus.FINISHED;
        });
    }
}
