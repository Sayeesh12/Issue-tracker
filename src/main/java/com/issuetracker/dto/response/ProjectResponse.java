package com.issuetracker.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
}