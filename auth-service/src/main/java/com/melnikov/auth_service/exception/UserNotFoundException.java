package com.melnikov.auth_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, выбрасываемое при попытке найти несуществующего пользователя.
 *
 * @author Мельников Никита
 */
public class UserNotFoundException extends GenericApiException {

    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public UserNotFoundException(String message) {
        super(message, 404, HttpStatus.NOT_FOUND);
    }
}
