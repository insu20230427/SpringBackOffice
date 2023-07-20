package com.example.springbackoffice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@ToString
public class ApiResult {
    // API result 반환을 위한 DTO
    // MSG와 status code(상태 코드)를 반환

    private String message;
    private HttpStatusCode httpStatusCode;

    @Builder
    public ApiResult(String message, HttpStatusCode httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
