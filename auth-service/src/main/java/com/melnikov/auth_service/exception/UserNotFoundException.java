package com.melnikov.auth_service.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GenericApiException {
    public UserNotFoundException(String message) {
        super(message, 404, HttpStatus.NOT_FOUND);
    }
}
