package com.ll.sbb20240111.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchTestConfig {
    @Bean
    public JobLauncherTestUtils helloJobLauncherTestUtils(Job helloJob) {
        /*
        테스트 하려면 JobLauncherTestUtils()랑 helloJob 묶어서 빈으로 등록하고 필요할 때 쓰면 된다.
         */
        JobLauncherTestUtils utils = new JobLauncherTestUtils();
        utils.setJob(helloJob);
        return utils;
    }

    @Bean
    public JobLauncherTestUtils hello2JobLauncherTestUtils(Job hello2Job) {
        JobLauncherTestUtils utils = new JobLauncherTestUtils();
        utils.setJob(hello2Job);
        return utils;
    }
}
