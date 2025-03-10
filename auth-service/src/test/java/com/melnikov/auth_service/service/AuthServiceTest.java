package com.melnikov.auth_service.service;

import com.melnikov.auth_service.dto.EmailResponse;
import com.melnikov.auth_service.dto.TokenResponse;
import com.melnikov.auth_service.exception.InvalidCodeException;
import com.melnikov.auth_service.exception.UserAlreadyExistsException;
import com.melnikov.auth_service.exception.UserNotFoundException;
import com.melnikov.auth_service.exception.CodeExpiredException;
import com.melnikov.auth_service.jwt.JwtTokenProvider;
import com.melnikov.auth_service.kafka.KafkaProducer;
import com.melnikov.auth_service.model.User;
import com.melnikov.auth_service.model.VerificationCode;
import com.melnikov.auth_service.repository.UserRepository;
import com.melnikov.auth_service.repository.VerificationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUser() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(verificationCodeRepository.save(any(VerificationCode.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmailResponse response = authService.registerUser(email);

        assertThat(response.info()).isEqualTo("Verification code send to email");
        assertThat(response.email()).isEqualTo(email);

        ArgumentCaptor<VerificationCode> captor = ArgumentCaptor.forClass(VerificationCode.class);
        verify(verificationCodeRepository).save(captor.capture());
        VerificationCode savedCode = captor.getValue();

        assertThat(savedCode.getCode()).matches("\\d{6}");
        assertThat(savedCode.getUser().getEmail()).isEqualTo(email);
        verify(kafkaProducer).sendEmail(email, savedCode.getCode());
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyExists() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> authService.registerUser(email));

        assertThat(exception.getMessage()).isEqualTo("User with email: " + email + " already exists");
    }

    @Test
    void shouldVerifyCodeAndGenerateToken() {
        String email = "test@example.com";
        String code = "123456";
        String token = "generated-jwt-token";

        User user = new User();
        user.setEmail(email);
        user.setVerified(false);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationCodeRepository.findByUserAndCode(user, code)).thenReturn(Optional.of(verificationCode));
        when(jwtTokenProvider.generateToken(email)).thenReturn(token);

        TokenResponse response = authService.verifyCode(email, code);

        assertThat(response.token()).isEqualTo(token);
        assertThat(user.isVerified()).isTrue();
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        String email = "nonexistent@example.com";
        String code = "123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> authService.verifyCode(email, code));

        assertThat(exception.getMessage()).isEqualTo("User with email: " + email + " already exists");
    }

    @Test
    void shouldThrowExceptionIfCodeIsInvalid() {
        String email = "test@example.com";
        String code = "123456";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationCodeRepository.findByUserAndCode(user, code)).thenReturn(Optional.empty());

        InvalidCodeException exception = assertThrows(InvalidCodeException.class, () -> authService.verifyCode(email, code));

        assertThat(exception.getMessage()).isEqualTo("Code: " + code + " is invalid");
    }

    @Test
    void shouldThrowExceptionIfCodeIsExpired() {
        String email = "test@example.com";
        String code = "123456";

        User user = new User();
        user.setEmail(email);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setCode(code);
        verificationCode.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(verificationCodeRepository.findByUserAndCode(user, code)).thenReturn(Optional.of(verificationCode));

        CodeExpiredException exception = assertThrows(CodeExpiredException.class, () -> authService.verifyCode(email, code));

        assertThat(exception.getMessage()).isEqualTo("Code: " + code + " is expired");
    }
}
