package com.melnikov.auth_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidCodeException extends GenericApiException {
    public InvalidCodeException(String message) {
        super(message, 400, HttpStatus.BAD_REQUEST);
    }
}
