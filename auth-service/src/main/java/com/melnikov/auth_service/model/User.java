package com.melnikov.auth_service.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Сущность, представляющая пользователя в системе.
 * Содержит информацию о пользователе, включая email и статус верификации.
 *
 * @author Мельников Никита
 */
@Data
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean isVerified;
}
