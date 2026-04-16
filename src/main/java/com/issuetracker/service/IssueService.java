package com.issuetracker.service;

import com.issuetracker.entity.Issue;
import com.issuetracker.enums.IssueStatus;
import org.springframework.data.domain.Page;

public interface IssueService {

    Issue createIssue(Issue issue, Long creatorId, Long projectId);

    Issue assignIssue(Long issueId, Long userId);

    Issue updateStatus(Long issueId, IssueStatus status, Long userId);

    Page<Issue> getProjectIssues(Long projectId, int page, int size);

    Page<Issue> getMyIssues(Long userId, int page, int size);
}