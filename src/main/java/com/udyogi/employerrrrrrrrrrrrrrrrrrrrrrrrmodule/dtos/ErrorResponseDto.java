package com.udyogi.employerrrrrrrrrrrrrrrrrrrrrrrrmodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor

public class ErrorResponseDto {

    private String appPath;
    private HttpStatus errorCode;
    private String errorMessage;
    private LocalDateTime errorTime;
}
