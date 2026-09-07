package com.address.config;
import com.address.exceptions.CustomException;
import com.address.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
public class CustomErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Exception decode(String methodKey, Response response) {

        HttpStatus httpStatus = HttpStatus.resolve(response.status());
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if (response.body() == null) {
            return new CustomException(
                    "Downstream service error: " + response.status(),
                    httpStatus
            );
        }
        try (InputStream is = response.body().asInputStream()) {
            // Parse into a raw Map to avoid HttpStatus deserialization issues.
            // The upstream "status" field comes as "404 NOT_FOUND" (a String),
            // which Jackson cannot map to the HttpStatus enum directly.
            @SuppressWarnings("unchecked")
            Map<String, Object> body = objectMapper.readValue(is, Map.class);
            String message = (String) body.getOrDefault("message", "Downstream service error");
            return new CustomException(message, httpStatus);
        } catch (IOException e) {
            return new CustomException(
                    "Failed to decode error response from downstream service",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
