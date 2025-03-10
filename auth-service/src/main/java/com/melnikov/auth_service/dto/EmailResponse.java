package com.melnikov.auth_service.dto;

/**
 * Ответ с информацией об отправленном коде подтверждения.
 *
 * @param info  информация о результате операции
 * @param email адрес электронной почты пользователя
 *
 * @author Мельников Никита
 */
public record EmailResponse(String info,
                            String email) {
}
