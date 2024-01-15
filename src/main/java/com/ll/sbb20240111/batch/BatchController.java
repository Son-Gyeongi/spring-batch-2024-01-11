package com.ll.sbb20240111.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Spring Batch Job을 실행하는 컨트롤러 클래스
 */
/*
미션 2
스프링부트가 시작되었을 때 자동으로 스프링배치 작업이 실행되도록 하지 말고
특정 메서드가 호출되면 그 때 해당 배치가 시작되도록 만들어주세요.
 */
@Controller
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;

    /**
     * "/batch/simple" 엔드포인트에 대한 GET 요청을 처리하는 메서드
     * @return 문자열 "runSimpleJob ok"를 응답으로 반환
     */
    @GetMapping("/hello")
    @ResponseBody
    public String runHelloJob() {
        // BatchService를 통해 간단한 Spring Batch Job을 실행
        batchService.runHelloJob();

        // 클라이언트에게 "runSimpleJob ok"라는 문자열을 응답으로 반환
        return "helloJob ok";
    }
}
