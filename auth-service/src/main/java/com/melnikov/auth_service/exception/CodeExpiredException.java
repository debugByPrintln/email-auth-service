package com.melnikov.auth_service.exception;

import org.springframework.http.HttpStatus;

/**
 * Исключение, выбрасываемое при истечении срока действия кода подтверждения.
 *
 * @author Мельников Никита
 */
public class CodeExpiredException extends GenericApiException {

    /**
     * Создает исключение с указанным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public CodeExpiredException(String message) {
        super(message, 400, HttpStatus.BAD_REQUEST);
    }
}
