package com.melnikov.auth_service.exception;

import com.melnikov.auth_service.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

/**
 * Глобальный обработчик исключений для контроллеров.
 * Преобразует исключения в стандартный формат ответа с кодом ошибки и сообщением.
 *
 * @author Мельников Никита
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение истечения срока действия кода.
     *
     * @param e исключение CodeExpiredException
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(CodeExpiredException.class)
    public ResponseEntity<ErrorResponse> handleCodeExpiredException(CodeExpiredException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getCode(), e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    /**
     * Обрабатывает исключение использования неверного кода.
     *
     * @param e исключение InvalidCodeException
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCodeException(InvalidCodeException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getCode(), e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    /**
     * Обрабатывает исключение попытки регистрации уже существующего пользователя.
     *
     * @param e исключение UserAlreadyExistsException
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getCode(), e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    /**
     * Обрабатывает исключение поиска несуществующего пользователя.
     *
     * @param e исключение UserNotFoundException
     * @return ответ с информацией об ошибке
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getCode(), e.getStatus(), e.getMessage()),
                e.getStatus()
        );
    }

    /**
     * Обрабатывает все остальные непредвиденные исключения.
     *
     * @param e исключение Exception
     * @return ответ с информацией о непредвиденной ошибке
     */
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
