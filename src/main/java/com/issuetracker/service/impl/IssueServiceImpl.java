package com.issuetracker.service.impl;

import com.issuetracker.dto.request.CreateIssueRequest;
import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.entity.Issue;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.exception.IssueNotFoundException;
import com.issuetracker.exception.ProjectNotFoundException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.mapper.IssueMapper;
import com.issuetracker.repository.IssueRepository;
import com.issuetracker.repository.ProjectRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.auth.AuthorizationService;
import com.issuetracker.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueMapper issueMapper;
    private final AuthorizationService authorizationService;

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

        return issueMapper.toResponse(issueRepository.save(issue));
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
                        org.springframework.data.domain.PageRequest.of(page, size)
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

        // Ensure assignee is part of project
        if (!issue.getProject().getMembers().contains(assignee)) {
            throw new IllegalArgumentException("Assignee must be a project member");
        }

        issue.setAssignee(assignee);

        return issueMapper.toResponse(issueRepository.save(issue));
    }

    // ---------------- UPDATE STATUS ----------------
    @Override
    public IssueResponse updateStatus(Long issueId, IssueStatus status, User user) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        authorizationService.canUpdateIssueStatus(user, issue);

        validateStatusTransition(issue.getStatus(), status);

        issue.setStatus(status);

        return issueMapper.toResponse(issueRepository.save(issue));
    }
    @Override
    public Page<IssueResponse> getMyIssues(User user, int page, int size) {

        return issueRepository.findByAssigneeId(
                        user.getId(),
                        org.springframework.data.domain.PageRequest.of(page, size)
                )
                .map(issueMapper::toResponse);
    }

    // ---------------- PRIVATE RULE ----------------
    private void validateStatusTransition(IssueStatus current, IssueStatus next) {

        // Enforce: OPEN → IN_PROGRESS → DONE

        if (current == IssueStatus.OPEN && next == IssueStatus.IN_PROGRESS) return;

        if (current == IssueStatus.IN_PROGRESS && next == IssueStatus.DONE) return;

        if (current == next) return;

        throw new IllegalStateException("Invalid status transition");
    }
}