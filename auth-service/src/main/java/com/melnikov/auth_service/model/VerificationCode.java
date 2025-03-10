package com.melnikov.auth_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая код подтверждения для пользователя.
 * Содержит код, срок действия и связь с пользователем.
 *
 * @author Мельников Никита
 */
@Data
@Entity
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String code;

    private LocalDateTime expiresAt;
}
