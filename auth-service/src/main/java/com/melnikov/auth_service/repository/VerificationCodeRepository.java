package com.melnikov.auth_service.repository;

import com.melnikov.auth_service.model.User;
import com.melnikov.auth_service.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью VerificationCode.
 * Предоставляет методы для поиска кодов подтверждения по пользователю и коду.
 *
 * @author Мельников Никита
 */
@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    /**
     * Ищет код подтверждения по пользователю и коду.
     *
     * @param user пользователь
     * @param code код подтверждения
     * @return Optional с найденным кодом подтверждения или пустой Optional, если код не найден
     */
    Optional<VerificationCode> findByUserAndCode(User user, String code);
}
