package edu.sigmaportal.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAccessKeyException extends RuntimeException {

    public InvalidAccessKeyException(String message) {
        super(message);
    }
}
