package com.employee.exceptions;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MissingParametersException extends RuntimeException{
    private String message;
    private HttpStatus status;

    public MissingParametersException(String message){
        this.message = message;
        this.status = HttpStatus.BAD_REQUEST;
    }
}
