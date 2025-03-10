package com.melnikov.auth_service.controller;

import com.melnikov.auth_service.dto.EmailResponse;
import com.melnikov.auth_service.dto.TokenResponse;
import com.melnikov.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<EmailResponse> register(@RequestParam String email) {
        EmailResponse emailResponse = authService.registerUser(email);
        return ResponseEntity.ok(emailResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<TokenResponse> verify(@RequestParam String email, @RequestParam String code) {
        TokenResponse tokenResponse = authService.verifyCode(email, code);
        return ResponseEntity.ok(tokenResponse);
    }
}
