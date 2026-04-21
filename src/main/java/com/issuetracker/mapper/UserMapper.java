package com.issuetracker.mapper;

import com.issuetracker.dto.request.RegisterRequest;
import com.issuetracker.dto.response.UserResponse;
import com.issuetracker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // ✅ DTO → Entity
    public User toEntity(RegisterRequest request) {
        if (request == null) return null;

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // ⚠️ password handled in service
        user.setPassword(request.getPassword());

        return user;
    }

    // ✅ Entity → DTO
    public UserResponse toResponse(User user) {
        if (user == null) return null;

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    // ✅ Update mapping (future-proof)
    public void updateEntityFromDto(RegisterRequest request, User user) {
        if (request == null || user == null) return;

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        // ❌ password update NOT handled here
    }
}