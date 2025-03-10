package com.melnikov.auth_service.exception;

import com.melnikov.auth_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CodeExpiredException.class)
    public ResponseEntity<ErrorResponse> handleCodeExpiredException(CodeExpiredException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getCode(), e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCodeException(InvalidCodeException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getCode(), e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getCode(), e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getCode(), e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return new ResponseEntity<>(
                new ErrorResponse(500,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected exception occurred: " + e.getClass() + " " + e.getMessage() + " " + Arrays.toString(e.getStackTrace())),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
