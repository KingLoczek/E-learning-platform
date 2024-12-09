package edu.sigmaportal.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadFileException extends RuntimeException {

    public BadFileException(String message) {
        super(message);
    }
}
