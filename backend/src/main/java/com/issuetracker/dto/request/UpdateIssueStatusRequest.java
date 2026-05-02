package com.issuetracker.dto.request;

import com.issuetracker.enums.IssueStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateIssueStatusRequest {

    @NotNull
    private IssueStatus status;
}