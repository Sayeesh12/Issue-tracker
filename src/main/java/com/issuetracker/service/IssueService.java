package com.issuetracker.service;

import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.enums.IssueStatus;
import org.springframework.data.domain.Page;

public interface IssueService {

    IssueResponse createIssue(String title, String description, Long projectId, Long userId);
    IssueResponse getIssueById(Long issueId);
    IssueResponse assignIssue(Long issueId, Long userId);

    IssueResponse updateStatus(Long issueId, IssueStatus status, Long userId);

    Page<IssueResponse> getProjectIssues(Long projectId, int page, int size);

    Page<IssueResponse> getMyIssues(Long userId, int page, int size);
}