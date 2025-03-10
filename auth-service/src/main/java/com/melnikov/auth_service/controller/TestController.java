package com.melnikov.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для тестирования защищенных эндпоинтов.
 *
 * @author Мельников Никита
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * Возвращает сообщение для защищенного эндпоинта.
     *
     * @return строка с сообщением о доступе к защищенному ресурсу
     */
    @GetMapping("/secured")
    public String secured() {
        return "This is a secured endpoint";
    }
}
