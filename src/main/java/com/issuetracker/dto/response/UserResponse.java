package com.issuetracker.dto.response;

import com.issuetracker.enums.Role;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
}