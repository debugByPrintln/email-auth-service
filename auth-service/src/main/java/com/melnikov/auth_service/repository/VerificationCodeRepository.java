package com.melnikov.auth_service.repository;

import com.melnikov.auth_service.model.User;
import com.melnikov.auth_service.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByUserAndCode(User user, String code);
}
