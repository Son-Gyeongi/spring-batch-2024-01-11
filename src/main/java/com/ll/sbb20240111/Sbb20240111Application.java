package com.ll.sbb20240111;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing // 엔티티에 날짜 업데이트를 활성화하는 어노테이션
@EnableScheduling // 스케줄링을 활성화하는 어노테이션
public class Sbb20240111Application {

    public static void main(String[] args) { // 애플리케이션의 진입점을 나타내는 메인 메서드
        SpringApplication.run(Sbb20240111Application.class, args);
        // SpringApplication.run을 호출하여 스프링 애플리케이션을 시작
    }

}
