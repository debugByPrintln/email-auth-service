package com.melnikov.auth_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Базовый класс для пользовательских исключений API.
 * Содержит код ошибки и HTTP-статус для обработки ошибок.
 *
 * @author Мельников Никита
 */
@Getter
public abstract class GenericApiException extends RuntimeException {

    private final int code;

    private final HttpStatus status;

    /**
     * Конструктор для создания исключения.
     *
     * @param message сообщение об ошибке
     * @param code    код ошибки
     * @param status  HTTP-статус ошибки
     */
    public GenericApiException(String message, int code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
