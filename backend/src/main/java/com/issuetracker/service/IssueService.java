package com.issuetracker.service;

import com.issuetracker.dto.request.CreateIssueRequest;
import com.issuetracker.dto.response.IssueActivityResponse;
import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.entity.User;
import com.issuetracker.enums.IssueStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IssueService {

    IssueResponse createIssue(Long projectId, CreateIssueRequest request, User user);

    IssueResponse getIssueById(Long issueId, User user);

    IssueResponse assignIssue(Long issueId, Long assigneeId, User currentUser);

    IssueResponse updateStatus(Long issueId, IssueStatus status, User user);

    Page<IssueResponse> getProjectIssues(Long projectId, int page, int size, User user);

    Page<IssueResponse> getMyIssues(User user, int page, int size);

    // ✅ NEW: Activity Logs API
    List<IssueActivityResponse> getIssueActivities(Long issueId, User user);
}