package com.issuetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateIssueRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Long projectId;
}