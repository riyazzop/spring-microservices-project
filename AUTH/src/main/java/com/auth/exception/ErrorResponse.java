package com.auth.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;

    public ErrorResponse(String message, HttpStatus status){
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
