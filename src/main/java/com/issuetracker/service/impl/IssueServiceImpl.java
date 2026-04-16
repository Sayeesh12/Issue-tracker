package com.issuetracker.service.impl;

import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.entity.Issue;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.exception.IssueNotFoundException;
import com.issuetracker.exception.InvalidStatusTransitionException;
import com.issuetracker.exception.ProjectNotFoundException;
import com.issuetracker.exception.UnauthorizedActionException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.repository.IssueRepository;
import com.issuetracker.repository.ProjectRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    // ✅ Create Issue
    @Override
    public IssueResponse createIssue(String title, String description, Long projectId, Long userId) {

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        boolean isMember = projectRepository
                .existsByIdAndMembers_Id(projectId, userId);

        if (!isMember) {
            throw new UnauthorizedActionException("User not part of project");
        }

        Issue issue = new Issue();
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setType(com.issuetracker.enums.IssueType.BUG); // or pass from DTO
        issue.setCreator(creator);
        issue.setProject(project);
        issue.setStatus(IssueStatus.OPEN);

        Issue saved = issueRepository.save(issue);

        return mapToResponse(saved);
    }
    @Override
    public IssueResponse getIssueById(Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        return mapToResponse(issue);
    }
    // ✅ Assign
    @Override
    public IssueResponse assignIssue(Long issueId, Long userId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        boolean isMember = projectRepository
                .existsByIdAndMembers_Id(issue.getProject().getId(), userId);

        if (!isMember) {
            throw new UnauthorizedActionException("User not part of project");
        }

        issue.setAssignee(user);

        return mapToResponse(issueRepository.save(issue));
    }

    // ✅ Update Status
    @Override
    public IssueResponse updateStatus(Long issueId, IssueStatus status, Long userId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        if (issue.getAssignee() == null ||
                !issue.getAssignee().getId().equals(userId)) {
            throw new UnauthorizedActionException("Only assignee can update status");
        }

        IssueStatus current = issue.getStatus();

        if (current == IssueStatus.OPEN && status == IssueStatus.DONE) {
            throw new InvalidStatusTransitionException("Invalid transition");
        }

        if (current == IssueStatus.DONE) {
            throw new InvalidStatusTransitionException("Already completed");
        }

        if (current == IssueStatus.IN_PROGRESS && status == IssueStatus.OPEN) {
            throw new InvalidStatusTransitionException("Cannot revert");
        }

        issue.setStatus(status);

        return mapToResponse(issueRepository.save(issue));
    }

    // ✅ Project Issues
    @Override
    public Page<IssueResponse> getProjectIssues(Long projectId, int page, int size) {

        return issueRepository.findByProjectId(projectId, PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    // ✅ My Issues
    @Override
    public Page<IssueResponse> getMyIssues(Long userId, int page, int size) {

        return issueRepository.findByAssigneeId(userId, PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    // 🔁 Mapping
    private IssueResponse mapToResponse(Issue issue) {
        return IssueResponse.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .type(issue.getType())
                .status(issue.getStatus())
                .build();
    }
}