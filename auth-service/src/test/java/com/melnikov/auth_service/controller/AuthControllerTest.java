package com.melnikov.auth_service.controller;

import com.melnikov.auth_service.config.SecurityConfig;
import com.melnikov.auth_service.dto.EmailResponse;
import com.melnikov.auth_service.dto.TokenResponse;
import com.melnikov.auth_service.exception.InvalidCodeException;
import com.melnikov.auth_service.exception.UserAlreadyExistsException;
import com.melnikov.auth_service.jwt.JwtTokenProvider;
import com.melnikov.auth_service.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtTokenProvider.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void shouldRegisterUser() throws Exception {
        String email = "test@example.com";
        EmailResponse emailResponse = new EmailResponse("Verification code send to email", email);

        when(authService.registerUser(email)).thenReturn(emailResponse);

        mockMvc.perform(post("/api/auth/register")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info").value("Verification code send to email"))
                .andExpect(jsonPath("$.email").value(email));

        verify(authService, times(1)).registerUser(email);
    }

    @Test
    void shouldReturnConflictIfUserAlreadyExists() throws Exception {
        String email = "test@example.com";

        when(authService.registerUser(email)).thenThrow(new UserAlreadyExistsException("User with email: " + email + " already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .param("email", email))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User with email: " + email + " already exists"));

        verify(authService, times(1)).registerUser(email);
    }

    @Test
    void shouldVerifyCodeAndReturnToken() throws Exception {
        String email = "test@example.com";
        String code = "123456";
        String token = "generated-jwt-token";

        TokenResponse tokenResponse = new TokenResponse(token);

        when(authService.verifyCode(email, code)).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/verify")
                        .param("email", email)
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));

        verify(authService, times(1)).verifyCode(email, code);
    }

    @Test
    void shouldReturnBadRequestIfCodeIsInvalid() throws Exception {
        String email = "test@example.com";
        String code = "123456";

        when(authService.verifyCode(email, code)).thenThrow(new InvalidCodeException("Code: " + code + " is invalid"));

        mockMvc.perform(post("/api/auth/verify")
                        .param("email", email)
                        .param("code", code))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Code: " + code + " is invalid"));

        verify(authService, times(1)).verifyCode(email, code);
    }
}
