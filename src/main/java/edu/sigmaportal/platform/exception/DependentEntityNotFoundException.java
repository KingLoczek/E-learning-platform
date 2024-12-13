package edu.sigmaportal.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DependentEntityNotFoundException extends RuntimeException {

    public DependentEntityNotFoundException(String message) {
        super(message);
    }
}
