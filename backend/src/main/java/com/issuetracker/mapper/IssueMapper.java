package com.issuetracker.mapper;

import com.issuetracker.dto.request.CreateIssueRequest;
import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.entity.Issue;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.enums.IssueType;
import org.springframework.stereotype.Component;

@Component
public class IssueMapper {

    public Issue toEntity(CreateIssueRequest request) {
        Issue issue = new Issue();

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setType(IssueType.valueOf(request.getType()));
        issue.setStatus(IssueStatus.OPEN);

        return issue;
    }
    public IssueResponse toResponse(Issue issue) {
        return IssueResponse.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .type(issue.getType())
                .projectId(issue.getProject().getId())
                .creatorId(issue.getCreator().getId())
                .assigneeId(
                        issue.getAssignee() != null ? issue.getAssignee().getId() : null
                )
                .build();
    }
}