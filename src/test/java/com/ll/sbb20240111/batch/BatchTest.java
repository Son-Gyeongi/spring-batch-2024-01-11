package com.ll.sbb20240111.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
        // hello2Job()을 실행해보고 싶다면
        hello3JobLauncherTestUtils.launchJob();
    }
}
