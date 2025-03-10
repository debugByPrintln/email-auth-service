package com.melnikov.auth_service.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(int code,
                            HttpStatus httpStatus,
                            String message) {
}
