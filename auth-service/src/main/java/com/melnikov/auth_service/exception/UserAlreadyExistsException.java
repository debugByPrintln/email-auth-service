package com.melnikov.auth_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, выбрасываемое при попытке регистрации уже существующего пользователя.
 *
 * @author Мельников Никита
 */
public class UserAlreadyExistsException extends GenericApiException {

    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public UserAlreadyExistsException(String message) {
        super(message, 409, HttpStatus.CONFLICT);
    }
}
