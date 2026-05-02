package com.issuetracker.controller;

import com.issuetracker.dto.request.LoginRequest;
import com.issuetracker.dto.request.RegisterRequest;
import com.issuetracker.dto.response.AuthResponse;
import com.issuetracker.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}