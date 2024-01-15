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
미션 1
세상에서 제일 간단한 배치 예제를 만들어서 실행해주세요.
Hello World 라는 텍스트가 배치에 의해서 출력되어야 합니다.
청크방식이 아닌 테스클릿 방식으로 만들어주세요.
 */
@Slf4j
@Configuration
public class HelloJobConfig {
    // Job 생성 메서드
    @Bean
    public Job helloJob(JobRepository jobRepository, Step helloStep1) { // 잡이 한개 있으면 이름 바꿔도 상관없지만 알아보기 쉽게 helloStep1로 바꾸었다.
        // JobBuilder를 사용하여 'helloJob'이라는 이름의 Job을 생성하고, simpleStep1 스텝을 시작으로 설정
        return new JobBuilder("helloJob", jobRepository)
                .start(helloStep1)
                .incrementer(new RunIdIncrementer()) // Job 파라미터를 위한 Incrementer 설정, 이거는 자동으로 실행될 때 작동한다.
                .build();
    }

    // Step 생성 메서드
    @JobScope
    @Bean
    public Step helloStep1(JobRepository jobRepository, Tasklet helloStep1Tasklet,
                           PlatformTransactionManager platformTransactionManager) { // helloStep1Tasklet1는 helloStep1Tasklet1 이다.
        // StepBuilder를 사용하여 'helloStep1Tasklet1'이라는 이름의 Step을 생성하고, Tasklet과 TransactionManager를 설정
        return new StepBuilder("helloStep1Tasklet", jobRepository)
                .tasklet(helloStep1Tasklet, platformTransactionManager)
                .build();
    }

    // Tasklet 생성 메서드
    @StepScope
    @Bean
    public Tasklet helloStep1Tasklet() {
        // Tasklet을 생성하고, execute 메서드에서 "Hello World"를 로깅하고 콘솔에 출력한 후 RepeatStatus.FINISHED를 반환
        return ((contribution, chunkContext) -> {
            log.info("Hello World");
            System.out.println("Hello World 1/1");
            return RepeatStatus.FINISHED;
        });
    }

    /*
    순서
    helloStep1Tasklet1() 빈등록
    -> helloStep1Tasklet1 객체가 연결되서 helloStep1()이 만들어지고 helloStep1 객체가 등록이 되서
    -> helloJob이 실행된다.
     */
}
