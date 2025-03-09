package com.melnikov.auth_service.controller;

import com.melnikov.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String register(@RequestParam String email) {
        return authService.registerUser(email);
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String email, @RequestParam String code) {
        return authService.verifyCode(email, code);
    }
}
