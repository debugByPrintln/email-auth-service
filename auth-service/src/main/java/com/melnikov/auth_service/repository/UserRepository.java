package com.melnikov.auth_service.repository;

import com.melnikov.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет методы для поиска пользователей по email и проверки существования email.
 *
 * @author Мельников Никита
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищет пользователя по email.
     *
     * @param email email пользователя
     * @return Optional с найденным пользователем или пустой Optional, если пользователь не найден
     */
    Optional<User> findByEmail(String email);

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param email email пользователя
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByEmail(String email);
}
