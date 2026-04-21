package com.issuetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateIssueRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long projectId;

    @NotNull
    private String type; // BUG / FEATURE / IMPROVEMENT
}