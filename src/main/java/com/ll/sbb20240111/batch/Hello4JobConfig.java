package com.ll.sbb20240111.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 배치 작업을 설정하는 Spring Batch Job Configuration 클래스
 * Job은 하나의 작업 단위를 나타낸다.
 */
/*
미션 5
hello4Job 생성
    hello4Step1 : 태스클릿이 아닌 청크기반작업을 수행
 */
@Slf4j
@Configuration
public class Hello4JobConfig {
    // hello4Job을 설정하는 메서드
    @Bean
    public Job hello4Job(
            JobRepository jobRepository,
            Step hello4Step1,
            Step hello4Step2,
            Step hello4Step3
    ) {
        // 먼저 hello4Step1과 hello4Step2가 병렬로 실행되는 Flow를 생성
        Flow flow1 = new FlowBuilder<SimpleFlow>("flow1")
                .start(hello4Step1)
                .build();

        Flow flow2 = new FlowBuilder<SimpleFlow>("flow2")
                .start(hello4Step2)
                .build();

        // 병렬로 실행되는 두 개의 Flow(flow1, flow2)를 하나로 합치는 병렬 Flow 생성
        Flow parallelFlow = new FlowBuilder<SimpleFlow>("parallelFlow")
                .split(new SimpleAsyncTaskExecutor()) // 병렬 실행을 위한 실행기 설정
                .add(flow1, flow2)
                .build();

        // 병렬 Flow(parallelFlow) 다음에 hello4Step3이 실행되는 Job 생성
        FlowJobBuilder jobBuilder = new JobBuilder("hello4Job", jobRepository)
                .start(parallelFlow)
                .next(hello4Step3)
                .end()
                .incrementer(new RunIdIncrementer());

        return jobBuilder.build();
    }

    // hello4Step1을 설정하는 메서드
    @Bean
    public Step hello4Step1(JobRepository jobRepository, ItemReader<String> hello4Step1Reader,
                            ItemWriter<String> hello4Step1Writer, PlatformTransactionManager platformTransactionManager) {
        // StepBuilder를 사용하여 'hello4Step1'이라는 이름의 Step을 생성하고, Chunk 기반의 Tasklet을 설정
        return new StepBuilder("hello4Step1")
                .<String, String>chunk(10) // 청크 기반으로 작업을 수행하며, 10개씩 처리
                .reader(hello4Step1Reader)
                .writer(hello4Step1Writer)
                .transactionManager(platformTransactionManager)
                .build();
    }

    // hello4Step2를 설정하는 메서드
    @Bean
    public Step hello4Step2(JobRepository jobRepository, Tasklet hello4Step2Tasklet, PlatformTransactionManager platformTransactionManager) {
        // StepBuilder를 사용하여 'hello4Step2Tasklet'이라는 이름의 Step을 생성하고, Tasklet과 TransactionManager를 설정
        return new StepBuilder("hello4Step2Tasklet", jobRepository)
                .tasklet(hello4Step2Tasklet, platformTransactionManager)
                .build();
    }

    // hello4Step2의 Tasklet을 설정하는 메서드
    @StepScope
    @Bean
    public Tasklet hello4Step2Tasklet() {
        // Tasklet을 생성하고, execute 메서드에서 "Hello World 3-2"를 출력한 후 RepeatStatus.FINISHED를 반환
        return ((contribution, chunkContext) -> {
            System.out.println("Hello World 3-2");
            return RepeatStatus.FINISHED;
        });
    }

    // hello4Step3을 설정하는 메서드
    @JobScope
    @Bean
    public Step hello4Step3(JobRepository jobRepository, Tasklet hello4Step3Tasklet, PlatformTransactionManager platformTransactionManager) {
        // StepBuilder를 사용하여 'hello4Step3Tasklet'이라는 이름의 Step을 생성하고, Tasklet과 TransactionManager를 설정
        return new StepBuilder("hello4Step3Tasklet", jobRepository)
                .tasklet(hello4Step3Tasklet, platformTransactionManager)
                .build();
    }

    // hello4Step3의 Tasklet을 설정하는 메서드
    @StepScope
    @Bean
    public Tasklet hello4Step3Tasklet() {
        // Tasklet을 생성하고, execute 메서드에서 "Hello World 3-3"을 출력한 후 RepeatStatus.FINISHED를 반환
        return ((contribution, chunkContext) -> {
            System.out.println("Hello World 3-3");
            return RepeatStatus.FINISHED;
        });
    }
}
