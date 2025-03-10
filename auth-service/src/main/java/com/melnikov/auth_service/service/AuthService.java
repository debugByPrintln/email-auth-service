package com.melnikov.auth_service.service;

import com.melnikov.auth_service.dto.EmailResponse;
import com.melnikov.auth_service.dto.TokenResponse;
import com.melnikov.auth_service.exception.CodeExpiredException;
import com.melnikov.auth_service.exception.InvalidCodeException;
import com.melnikov.auth_service.exception.UserAlreadyExistsException;
import com.melnikov.auth_service.exception.UserNotFoundException;
import com.melnikov.auth_service.jwt.JwtTokenProvider;
import com.melnikov.auth_service.kafka.KafkaProducer;
import com.melnikov.auth_service.model.User;
import com.melnikov.auth_service.model.VerificationCode;
import com.melnikov.auth_service.repository.UserRepository;
import com.melnikov.auth_service.repository.VerificationCodeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Log4j2
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final KafkaProducer kafkaProducer;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthService(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository,
                       KafkaProducer kafkaProducer, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.kafkaProducer = kafkaProducer;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public EmailResponse registerUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email: " + email + " already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setVerified(false);
        userRepository.save(user);

        String code = generateCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        verificationCodeRepository.save(verificationCode);

        kafkaProducer.sendEmail(email, code);

        return new EmailResponse("Verification code send to email", email);
    }

    public TokenResponse verifyCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " already exists"));

        VerificationCode verificationCode = verificationCodeRepository.findByUserAndCode(user, code)
                .orElseThrow(() -> new InvalidCodeException("Code: " + code + " is invalid"));

        if (LocalDateTime.now().isAfter(verificationCode.getExpiresAt())) {
            throw new CodeExpiredException("Code: " + code + " is expired");
        }

        user.setVerified(true);
        userRepository.save(user);

        return new TokenResponse(jwtTokenProvider.generateToken(email));
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
