package com.melnikov.auth_service.dto;

import org.springframework.http.HttpStatus;

/**
 * Ответ с информацией об ошибке.
 *
 * @param code       код ошибки
 * @param httpStatus HTTP-статус ошибки
 * @param message    сообщение об ошибке
 *
 * @author Мельников Никита
 */
public record ErrorResponse(int code,
                            HttpStatus httpStatus,
                            String message) {
}
