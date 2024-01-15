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
Job 2개이고 spring.batch.job.enable=true 이면 꼭 spring.batch.job.name 이 필수
잡이 2개면 자동으로 실행되게 하려는 건 딱 하나만 정할 수 있다.

Step 빈 등록시에는 @JobScope, Tasklet 빈 등록시에는 @StepScope 필수, 무조건 붙이기 -> 잡 파라미터의 유연한 사용과 관계가 있다.
Job 만들 때는 붙이는거 아니다.
 */
@Slf4j
@Configuration
public class Hello2JobConfig {
    // Job 생성 메서드
    @Bean
    public Job hello2Job(JobRepository jobRepository, Step hello2Step1, Step hello2Step2) { // 잡이 한개 있으면 이름 바꿔도 상관없지만 알아보기 쉽게 hello2Step1로 바꾸었다.
        // JobBuilder를 사용하여 'hello2Job'이라는 이름의 Job을 생성하고, simpleStep1 스텝을 시작으로 설정
        return new JobBuilder("hello2Job", jobRepository)
                .start(hello2Step1)
                .next(hello2Step2)
                .incrementer(new RunIdIncrementer()) // Job 파라미터를 위한 Incrementer 설정, 이거는 자동으로 실행될 때 작동한다.
                .build();
    }

    // Step 생성 메서드
    @JobScope
    @Bean
    public Step hello2Step1(JobRepository jobRepository, Tasklet hello2Step1Tasklet,
                           PlatformTransactionManager platformTransactionManager) { // hello2Step1Tasklet1는 hello2Step1Tasklet1 이다.
        // StepBuilder를 사용하여 'hello2Step1Tasklet1'이라는 이름의 Step을 생성하고, Tasklet과 TransactionManager를 설정
        return new StepBuilder("hello2Step1Tasklet", jobRepository)
                .tasklet(hello2Step1Tasklet, platformTransactionManager)
                .build();
    }

    // Tasklet 생성 메서드
    @StepScope
    @Bean
    public Tasklet hello2Step1Tasklet() {
        // Tasklet을 생성하고, execute 메서드에서 "Hello World"를 로깅하고 콘솔에 출력한 후 RepeatStatus.FINISHED를 반환
        return ((contribution, chunkContext) -> {
            System.out.println("Hello World 2-1");
            return RepeatStatus.FINISHED;
        });
    }

    // Step 생성 메서드
    @JobScope
    @Bean
    public Step hello2Step2(JobRepository jobRepository, Tasklet hello2Step2Tasklet,
                            PlatformTransactionManager platformTransactionManager) { // hello2Step2Tasklet1는 hello2Step2Tasklet1 이다.
        // StepBuilder를 사용하여 'hello2Step2Tasklet1'이라는 이름의 Step을 생성하고, Tasklet과 TransactionManager를 설정
        return new StepBuilder("hello2Step2Tasklet", jobRepository)
                .tasklet(hello2Step2Tasklet, platformTransactionManager)
                .build();
    }

    // Tasklet 생성 메서드
    @StepScope
    @Bean
    public Tasklet hello2Step2Tasklet() {
        // Tasklet을 생성하고, execute 메서드에서 "Hello World"를 로깅하고 콘솔에 출력한 후 RepeatStatus.FINISHED를 반환
        return ((contribution, chunkContext) -> {
            System.out.println("Hello World 2-2");
            return RepeatStatus.FINISHED;
        });
    }
}
