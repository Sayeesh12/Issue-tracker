package com.issuetracker.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectMemberResponse {

    private Long id;
    private String name;
    private String email;
}
