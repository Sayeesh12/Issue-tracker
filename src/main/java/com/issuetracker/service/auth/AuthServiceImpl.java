package com.issuetracker.service.auth;

import com.issuetracker.dto.request.LoginRequest;
import com.issuetracker.dto.request.RegisterRequest;
import com.issuetracker.dto.response.AuthResponse;
import com.issuetracker.entity.User;
import com.issuetracker.enums.Role;
import com.issuetracker.exception.InvalidCredentialsException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.exception.DuplicateResourceException;
import com.issuetracker.mapper.UserMapper;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        // ✅ use mapper
        User user = userMapper.toEntity(request);

        // ✅ service responsibility
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public Long getCurrentUserId(Authentication auth) {
        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getId();
    }
}