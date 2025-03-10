package com.melnikov.auth_service.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends GenericApiException {
    public UserAlreadyExistsException(String message) {
        super(message, 409, HttpStatus.CONFLICT);
    }
}
