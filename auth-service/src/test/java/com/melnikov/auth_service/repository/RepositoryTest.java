package com.melnikov.auth_service.repository;

import com.melnikov.auth_service.model.User;
import com.melnikov.auth_service.model.VerificationCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
public class RepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test-db")
            .withUsername("postgres")
            .withPassword("postgres");

    private UserRepository userRepository;

    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    public RepositoryTest(UserRepository userRepository, VerificationCodeRepository verificationCodeRepository) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        verificationCodeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testShouldFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setVerified(false);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testShouldReturnEmptyOptionalIfEmailNotFound() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertThat(foundUser).isEmpty();
    }

    @Test
    void testShouldFindByUserAndCode() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setVerified(false);
        userRepository.save(user);

        VerificationCode code = new VerificationCode();
        code.setUser(user);
        code.setCode("123456");
        code.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        verificationCodeRepository.save(code);

        Optional<VerificationCode> foundCode = verificationCodeRepository.findByUserAndCode(user, "123456");

        assertThat(foundCode).isPresent();
        assertThat(foundCode.get().getCode()).isEqualTo("123456");
    }

    @Test
    void testShouldReturnEmptyOptionalIfCodeNotFound() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setVerified(false);
        userRepository.save(user);

        Optional<VerificationCode> foundCode = verificationCodeRepository.findByUserAndCode(user, "999999");

        assertThat(foundCode).isEmpty();
    }
}
