package com.melnikov.auth_service.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Фильтр для обработки JWT-токенов в HTTP-запросах.
 * Извлекает токен, проверяет его валидность и устанавливает аутентификацию в контекст безопасности.
 *
 * @author Мельников Никита
 */
@Component
@Log4j2
public class JwtTokenFilter extends OncePerRequestFilter {

    private  final JwtTokenProvider jwtTokenProvider;

    /**
     * Конструктор для внедрения зависимости JwtTokenProvider.
     *
     * @param jwtTokenProvider провайдер для работы с JWT-токенами
     */
    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Выполняет фильтрацию каждого HTTP-запроса.
     * Проверяет наличие и валидность JWT-токена, устанавливает аутентификацию в контекст безопасности.
     *
     * @param request     HTTP-запрос
     * @param response    HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException если возникает ошибка при фильтрации
     * @throws IOException      если возникает ошибка ввода/вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token == null) {
            log.info("No token found in the request");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Token extracted from request: {}", token);

        if (!jwtTokenProvider.validateToken(token)) {
            log.info("Token is invalid or expired");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(token);

        log.info("Creating JwtAuthentication with email: {} and authorities: {}", email, authorities);
        var authentication = new JwtAuthentication(email, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    /**
     * Извлекает JWT-токен из заголовка Authorization.
     *
     * @param request HTTP-запрос
     * @return JWT-токен или null, если токен отсутствует или некорректен
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
