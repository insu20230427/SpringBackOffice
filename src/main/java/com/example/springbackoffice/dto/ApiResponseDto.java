package com.example.springbackoffice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {

    private int status;
    private String message;
    private Object data;
    private HttpStatusCode httpStatusCode;
    private String msg;
    private Integer statusCode;

    public ApiResponseDto(String msg, Integer statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public ApiResponseDto(int status, String message) { // data까지 보내지 않을 때
        this.status = status;
        this.message = message;
    }

    public ApiResponseDto(int status,String message, Object data) { // data까지 실어보낼 때
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @Builder
    public ApiResponseDto(String message, HttpStatusCode httpStatusCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public ApiResponseDto() {
    }
}