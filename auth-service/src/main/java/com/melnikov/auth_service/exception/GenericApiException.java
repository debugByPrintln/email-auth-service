package com.melnikov.auth_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class GenericApiException extends RuntimeException {

    private final int code;

    private final HttpStatus status;

    public GenericApiException(String message, int code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
