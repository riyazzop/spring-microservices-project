package com.address.config;

import com.address.exceptions.CustomException;
import com.address.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;


public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        if (response.body() == null) {
            return new CustomException(
                    "Downstream service error: " + response.status(),
                    HttpStatus.valueOf(response.status())
            );
        }

        try (InputStream is = response.body().asInputStream()) {
            ErrorResponse errorResponse = objectMapper.readValue(is, ErrorResponse.class);
            return new CustomException(errorResponse.getMessage(), errorResponse.getStatus());
        } catch (IOException e) {
            return new CustomException(
                    "Failed to decode error response from downstream service",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}


