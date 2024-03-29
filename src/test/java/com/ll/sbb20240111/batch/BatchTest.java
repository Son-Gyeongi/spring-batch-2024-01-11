package com.ll.sbb20240111.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

/*
미션 3
특정 잡을 테스트 케이스에서 명시적으로 호출
 */
@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test") // 개발이랑 같은 db를 쓰면 안 좋아서 application-test.yml로 분리
public class BatchTest {
    //  테스트 환경에서 배치 실행하는 방법, 각 Job 마다 JobLauncherTestUtils 빈이 필요 -> BatchTestConfig에 만들면 된다.
    @Autowired
    private JobLauncherTestUtils helloJobLauncherTestUtils;
    @Autowired
    private JobLauncherTestUtils hello2JobLauncherTestUtils;
    @Autowired
    private JobLauncherTestUtils hello3JobLauncherTestUtils;
    @Autowired
    private JobLauncherTestUtils hello4JobLauncherTestUtils;
    @Autowired
    private JobLauncherTestUtils hello5JobLauncherTestUtils;
    @Autowired
    private JobLauncherTestUtils makeProductLogJobLauncherTestUtils;

    @DisplayName("helloJob")
    @Test
    public void t1() throws Exception {
        // helloJob()을 실행해보고 싶다면
        helloJobLauncherTestUtils.launchJob();
    }

    @DisplayName("hello2Job")
    @Test
    public void t2() throws Exception {
        // hello2Job()을 실행해보고 싶다면
        hello2JobLauncherTestUtils.launchJob();
    }

    @DisplayName("hello3Job")
    @Test
    public void t3() throws Exception {
        // hello3Job()을 실행해보고 싶다면
        hello3JobLauncherTestUtils.launchJob();
    }

    @DisplayName("hello4Job")
    @Test
    public void t4() throws Exception {
        // hello4Job()을 실행해보고 싶다면
        hello4JobLauncherTestUtils.launchJob();
    }

    @DisplayName("hello5Job")
    @Test
    public void t5() throws Exception {
        // hello5Job()을 실행해보고 싶다면
        hello5JobLauncherTestUtils.launchJob();
    }

    @DisplayName("makeProductLogJob")
    @Test
    public void t6() throws Exception {
        // 시각에서 년-월-일만 나오게 자름
        String startDate = LocalDateTime.now().minusDays(1).toString().substring(0, 10) + " 00:00:00.000000";
        String endDate = LocalDateTime.now().minusDays(1).toString().substring(0, 10) + " 23:59:59.999999";

        // 파라미터 생성 - 잡 파라미터를 잡 실행하는 곳에 만든다.
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("startDate", startDate)
                .addString("endDate", endDate)
                .toJobParameters();

        // makeProductLogJob()을 실행해보고 싶다면
        makeProductLogJobLauncherTestUtils.launchJob(jobParameters);
    }
}
