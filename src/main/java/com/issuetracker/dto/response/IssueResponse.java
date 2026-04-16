package com.issuetracker.dto.response;

import com.issuetracker.enums.IssueStatus;
import com.issuetracker.enums.IssueType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IssueResponse {

    private Long id;
    private String title;
    private String description;
    private IssueType type;
    private IssueStatus status;
}