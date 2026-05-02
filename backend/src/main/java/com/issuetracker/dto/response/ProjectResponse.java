package com.issuetracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;

    private List<Long> memberIds;
}