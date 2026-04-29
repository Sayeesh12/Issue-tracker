package com.issuetracker.service.impl;

import com.issuetracker.dto.request.CreateIssueRequest;
import com.issuetracker.dto.response.IssueActivityResponse;
import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.entity.Issue;
import com.issuetracker.entity.IssueActivity;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import com.issuetracker.enums.ActivityAction;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.exception.IssueNotFoundException;
import com.issuetracker.exception.ProjectNotFoundException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.mapper.IssueActivityMapper;
import com.issuetracker.mapper.IssueMapper;
import com.issuetracker.repository.IssueActivityRepository;
import com.issuetracker.repository.IssueRepository;
import com.issuetracker.repository.ProjectRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.IssueService;
import com.issuetracker.service.auth.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueMapper issueMapper;
    private final AuthorizationService authorizationService;
    private final IssueActivityRepository activityRepository;
    private final IssueActivityMapper issueActivityMapper;

    // ---------------- CREATE ISSUE ----------------
    @Override
    public IssueResponse createIssue(Long projectId, CreateIssueRequest request, User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        authorizationService.canCreateIssue(user, project);

        Issue issue = issueMapper.toEntity(request);
        issue.setProject(project);
        issue.setCreator(user);
        issue.setStatus(IssueStatus.OPEN);

        Issue saved = issueRepository.save(issue);

        // ✅ Activity Log
        activityRepository.save(
                IssueActivity.builder()
                        .issue(saved)
                        .action(ActivityAction.CREATED)
                        .performedBy(user)
                        .details("Issue created")
                        .build()
        );

        return issueMapper.toResponse(saved);
    }

    // ---------------- GET ISSUE ----------------
    @Override
    public IssueResponse getIssueById(Long issueId, User user) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        authorizationService.canViewIssue(user, issue);

        return issueMapper.toResponse(issue);
    }

    // ---------------- GET PROJECT ISSUES ----------------
    @Override
    public Page<IssueResponse> getProjectIssues(Long projectId, int page, int size, User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        authorizationService.canViewProject(user, project);

        return issueRepository.findByProjectId(
                        projectId,
                        PageRequest.of(page, size)
                )
                .map(issueMapper::toResponse);
    }

    // ---------------- ASSIGN ISSUE ----------------
    @Override
    public IssueResponse assignIssue(Long issueId, Long assigneeId, User currentUser) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        authorizationService.canAssignIssue(currentUser, issue);

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!issue.getProject().getMembers().contains(assignee)) {
            throw new IllegalArgumentException("Assignee must be a project member");
        }

        issue.setAssignee(assignee);

        Issue updated = issueRepository.save(issue);

        // ✅ Activity Log
        activityRepository.save(
                IssueActivity.builder()
                        .issue(updated)
                        .action(ActivityAction.ASSIGNED)
                        .performedBy(currentUser)
                        .details("Assigned to " + assignee.getEmail())
                        .build()
        );

        return issueMapper.toResponse(updated);
    }

    // ---------------- UPDATE STATUS ----------------
    @Override
    public IssueResponse updateStatus(Long issueId, IssueStatus status, User user) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        authorizationService.canUpdateIssueStatus(user, issue);

        IssueStatus oldStatus = issue.getStatus();

        validateStatusTransition(oldStatus, status);

        issue.setStatus(status);

        Issue updated = issueRepository.save(issue);

        // ✅ Activity Log
        activityRepository.save(
                IssueActivity.builder()
                        .issue(updated)
                        .action(ActivityAction.STATUS_CHANGED)
                        .performedBy(user)
                        .details(oldStatus + " → " + status)
                        .build()
        );

        return issueMapper.toResponse(updated);
    }

    // ---------------- GET MY ISSUES ----------------
    @Override
    public Page<IssueResponse> getMyIssues(User user, int page, int size) {

        return issueRepository.findByAssigneeId(
                        user.getId(),
                        PageRequest.of(page, size)
                )
                .map(issueMapper::toResponse);
    }

    // ---------------- GET ISSUE ACTIVITIES ----------------
    @Override
    public List<IssueActivityResponse> getIssueActivities(Long issueId, User user) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        authorizationService.canViewIssue(user, issue);

        return activityRepository.findByIssueOrderByCreatedAtDesc(issue)
                .stream()
                .map(issueActivityMapper::toResponse)
                .toList();
    }

    // ---------------- PRIVATE RULE ----------------
    private void validateStatusTransition(IssueStatus current, IssueStatus next) {

        if (current == IssueStatus.OPEN && next == IssueStatus.IN_PROGRESS) return;

        if (current == IssueStatus.IN_PROGRESS && next == IssueStatus.DONE) return;

        if (current == next) return;

        throw new IllegalStateException("Invalid status transition");
    }
}