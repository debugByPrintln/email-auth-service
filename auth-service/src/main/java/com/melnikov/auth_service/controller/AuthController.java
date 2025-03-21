package com.melnikov.auth_service.controller;

import com.melnikov.auth_service.dto.EmailResponse;
import com.melnikov.auth_service.dto.TokenResponse;
import com.melnikov.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов аутентификации.
 * Предоставляет эндпоинты для регистрации пользователей и верификации кодов.
 *
 * @author Мельников Никита
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Конструктор для внедрения зависимости AuthService.
     *
     * @param authService сервис для выполнения операций аутентификации
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Регистрирует нового пользователя по email.
     *
     * @param email адрес электронной почты пользователя
     * @return ответ с информацией об отправленном коде подтверждения
     */
    @PostMapping("/register")
    public ResponseEntity<EmailResponse> register(@RequestParam String email) {
        EmailResponse emailResponse = authService.registerUser(email);
        return ResponseEntity.ok(emailResponse);
    }

    /**
     * Верифицирует код подтверждения для пользователя.
     *
     * @param email адрес электронной почты пользователя
     * @param code  код подтверждения
     * @return ответ с JWT-токеном при успешной верификации
     */
    @PostMapping("/verify")
    public ResponseEntity<TokenResponse> verify(@RequestParam String email, @RequestParam String code) {
        TokenResponse tokenResponse = authService.verifyCode(email, code);
        return ResponseEntity.ok(tokenResponse);
    }
}
