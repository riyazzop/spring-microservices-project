package com.address.exceptions;

// import lombok.AllArgsConstructor;
// import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends  RuntimeException {
    private String message;
    private HttpStatus status;

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }

}
