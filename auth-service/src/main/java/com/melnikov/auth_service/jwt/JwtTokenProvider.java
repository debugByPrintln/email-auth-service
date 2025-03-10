package com.melnikov.auth_service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Провайдер для работы с JWT-токенами.
 * Генерирует, валидирует и извлекает данные из JWT-токенов.
 *
 * @author Мельников Никита
 */
@Component
@Log4j2
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Генерирует JWT-токен для указанного email.
     *
     * @param email адрес электронной почты пользователя
     * @return сгенерированный JWT-токен
     */
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", List.of("ROLE_USER"))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Извлекает email из JWT-токена.
     *
     * @param token JWT-токен
     * @return email пользователя
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Извлекает права доступа (роли) из JWT-токена.
     *
     * @param token JWT-токен
     * @return список прав доступа пользователя
     */
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> roles = claims.get("roles", List.class);
        if (roles == null || roles.isEmpty()) {
            log.info("No roles found in token");
            return Collections.emptyList();
        }

        log.info("Roles extracted from token: {}", roles);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Проверяет валидность JWT-токена.
     *
     * @param token JWT-токен
     * @return true, если токен валиден, иначе false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Возвращает секретный ключ для подписи JWT-токенов.
     *
     * @return секретный ключ
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
