package com.melnikov.auth_service.dto;

/**
 * Ответ с JWT-токеном.
 *
 * @param token JWT-токен для аутентификации
 *
 * @author Мельников Никита
 */
public record TokenResponse(String token) {
}
