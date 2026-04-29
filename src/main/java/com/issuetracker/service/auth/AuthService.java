package com.issuetracker.service.auth;

import com.issuetracker.dto.request.LoginRequest;
import com.issuetracker.dto.request.RegisterRequest;
import com.issuetracker.dto.response.AuthResponse;
import com.issuetracker.entity.User;
import org.springframework.security.core.Authentication;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    User getCurrentUser(Authentication authentication);
}