package com.melnikov.auth_service.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Класс для представления аутентификации на основе JWT.
 * Содержит email пользователя и его права доступа.
 *
 * @author Мельников Никита
 */
public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String email;

    /**
     * Создает объект аутентификации на основе JWT.
     *
     * @param email       email пользователя
     * @param authorities права доступа пользователя
     */
    public JwtAuthentication(String email, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = email;
        setAuthenticated(true);
    }

    /**
     * Возвращает учетные данные (всегда null для JWT).
     *
     * @return null
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Возвращает основной идентификатор (email пользователя).
     *
     * @return email пользователя
     */
    @Override
    public Object getPrincipal() {
        return email;
    }
}
