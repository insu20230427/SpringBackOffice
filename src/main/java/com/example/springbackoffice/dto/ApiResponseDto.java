package com.example.springbackoffice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {

    private int status;
    private String message;
    private Object data;

    public ApiResponseDto(int status, String message) { // data까지 보내지 않을 때
        this.status = status;
        this.message = message;
    }

    public ApiResponseDto(int status,String message, Object data) { // data까지 실어보낼 때
        this.status = status;
        this.message = message;
        this.data = data;
    }
}


