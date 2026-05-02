package com.issuetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ProjectRequest {

    @NotBlank
    private String name;

    private String description;

    // optional: initial members
    private List<Long> memberIds;
}