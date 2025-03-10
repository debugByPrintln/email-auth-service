package com.melnikov.auth_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, выбрасываемое при использовании неверного кода подтверждения.
 *
 * @author Мельников Никита
 */
public class InvalidCodeException extends GenericApiException {

    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public InvalidCodeException(String message) {
        super(message, 400, HttpStatus.BAD_REQUEST);
    }
}
