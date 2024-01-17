package com.ll.sbb20240111.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Spring Batch Job을 실행하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class BatchService {
    // Spring Batch Job을 실행하는 데 필요한 JobLauncher를 주입받음
    private final JobLauncher jobLauncher;
    // 실행할 Spring Batch Job을 주입받음
    private final Job helloJob;
    private final Job makeProductLogJob;

    /**
     * 간단한 Spring Batch Job을 실행하는 메서드
     */
    public void runHelloJob() {
        try {
            // 내가 직접 실행할 때는 파라미터를 직접 구성한다. 직접 파라미터 적으면
            // 잡의 .incrementer()이 작동하지 않는다.
            /*
            Spring Batch Job을 실행하기 위해 필요한 JobParameters를 구성
            .toJobParameters() 메서드는 파라미터가 없는 기본 JobParameters를 생성함
             */
            JobParameters jobParameters = new JobParametersBuilder()
                    .toJobParameters(); // 파라미터는 사실상 없는거랑 마찬가지이다.
            // JobLauncher를 사용하여 simpleJob을 실행하고, 구성한 JobParameters 전달
            jobLauncher.run(helloJob, jobParameters);
        } catch (Exception e) {
            // 예외 발생 시 스택 트레이스를 출력
            e.printStackTrace();
        }
    }

    public void runMakeProductLogJob(LocalDateTime startDate_, LocalDateTime endDate_) {
        try {
            String startDate = startDate_.toString().substring(0, 10) + "00:00:00.000000";
            String endDate = startDate_.toString().substring(0, 10) + "23:59:59.999999";

            // 파라미터 생성
            // 잡 파라미터를 이용해서 로깅의 구간(날짜 기준)을 설정
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("startDate", startDate)
                    .addString("endDate", endDate)
                    .toJobParameters();

            jobLauncher.run(makeProductLogJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
