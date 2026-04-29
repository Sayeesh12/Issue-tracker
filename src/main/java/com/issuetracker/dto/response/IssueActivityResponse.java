package com.issuetracker.dto.response;

import com.issuetracker.enums.ActivityAction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IssueActivityResponse {

    private ActivityAction action;
    private String performedBy;
    private String details;
    private LocalDateTime createdAt;
}