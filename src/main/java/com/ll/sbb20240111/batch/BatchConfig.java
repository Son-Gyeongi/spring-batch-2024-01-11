package com.ll.sbb20240111.batch;

import com.ll.sbb20240111.global.app.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BatchConfig {
    private final BatchService batchService;

    // 매일 새벽 1시에 실행되는 스케줄 메서드 cron = "초 분 시간 일 월 주 년"
    @Scheduled(cron = "0 0 1 * * *") // 운영
//    @Scheduled(cron = "0 * * * * *") // 테스트 - 매 분의 0초(1분마다 실행)에 해당 메서드를 실행, 0초/ 매 분/ 매 시간/ 매일/ 매월/ 매주/ 매 년
    public void runMakeProductLogJob() {
        // 운영 환경이 아닌 경우 실행하지 않음
        if (AppConfig.isNotProd()) return;

        // 어제의 시작 시간과 종료 시간을 계산
        LocalDateTime startDate = LocalDateTime
                .now()
                .minusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        LocalDateTime endDate = LocalDateTime
                .now()
                .minusDays(1)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999);

        // BatchService의 runMakeProductLogJob 메서드를 어제의 기간으로 실행
        batchService.runMakeProductLogJob(startDate, endDate);
    }
}
