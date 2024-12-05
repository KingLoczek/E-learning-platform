package edu.sigmaportal.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SetupNotFinishedException extends RuntimeException {

    public SetupNotFinishedException(String message) {
        super(message);
    }
}
