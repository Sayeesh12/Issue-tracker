package com.issuetracker.service.auth;

import com.issuetracker.dto.request.LoginRequest;
import com.issuetracker.dto.request.RegisterRequest;
import com.issuetracker.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}