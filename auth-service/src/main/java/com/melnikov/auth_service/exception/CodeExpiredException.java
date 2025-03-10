package com.melnikov.auth_service.exception;

import org.springframework.http.HttpStatus;

public class CodeExpiredException extends GenericApiException {
    public CodeExpiredException(String message) {
        super(message, 400, HttpStatus.BAD_REQUEST);
    }
}
