package com.melnikov.auth_service.config;

import com.melnikov.auth_service.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности приложения.
 * Настройка правил доступа, фильтров и политик аутентификации.
 *
 * @author Мельников Никита
 */
@Configuration
public class SecurityConfig {

    private final JwtTokenFilter filter;

    /**
     * Конструктор для внедрения зависимости JwtTokenFilter.
     *
     * @param filter фильтр для обработки JWT-токенов
     */
    @Autowired
    public SecurityConfig(JwtTokenFilter filter) {
        this.filter = filter;
    }

    /**
     * Настройка цепочки фильтров безопасности.
     * Отключает CSRF, настраивает stateless-сессии и правила доступа.
     *
     * @param http конфигуратор безопасности HTTP-запросов
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception если возникает ошибка при настройке
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(matcher -> {
                    matcher
                            .requestMatchers("/api/auth/register", "/api/auth/verify")
                            .permitAll();
                    matcher
                            .requestMatchers("/api/test/secured")
                            .hasRole("USER");
                    matcher
                            .anyRequest()
                            .authenticated();
                })
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
