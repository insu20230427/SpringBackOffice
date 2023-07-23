package com.example.springbackoffice.config;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
// 이런 식으로 빈을 수동으로 등록하면, 추가적인 혹은 개발자가 원하는 디테일한 설정을 만질 수 있다.
// restTemplate 으로 외부 API 를 호출할 수 있다.
@Configuration
public class RestTemplateConfig  {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                // RestTemplate 으로 외부 API 호출 시 일정 시간이 지나도 응답이 없을 때
                // 무한 대기 상태 방지를 위해 강제 종료 설정
                .setConnectTimeout(Duration.ofSeconds(5)) // 5초
                .setReadTimeout(Duration.ofSeconds(5)) // 5초
                .build();
    }
}
