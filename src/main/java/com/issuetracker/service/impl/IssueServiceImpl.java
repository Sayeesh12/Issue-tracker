package com.issuetracker.service.impl;

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
    public Issue createIssue(Issue issue, Long creatorId, Long projectId) {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        // 🔥 Validate creator is part of project
        boolean isMember = projectRepository
                .existsByIdAndMembers_Id(projectId, creatorId);

        if (!isMember) {
            throw new UnauthorizedActionException("User not part of project");
        }

        issue.setCreator(creator);              // 🔥 IMPORTANT
        issue.setProject(project);
        issue.setStatus(IssueStatus.OPEN);

        return issueRepository.save(issue);
    }

    // ✅ Assign Issue
    @Override
    public Issue assignIssue(Long issueId, Long userId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 🔥 Validate user belongs to project
        boolean isMember = projectRepository
                .existsByIdAndMembers_Id(issue.getProject().getId(), userId);

        if (!isMember) {
            throw new UnauthorizedActionException("User not part of project");
        }

        issue.setAssignee(user);

        return issueRepository.save(issue);
    }

    // ✅ Update Status (Workflow + Ownership)
    @Override
    public Issue updateStatus(Long issueId, IssueStatus status, Long userId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        // 🔥 Only assignee can update
        if (issue.getAssignee() == null ||
                !issue.getAssignee().getId().equals(userId)) {
            throw new UnauthorizedActionException("Only assignee can update status");
        }

        IssueStatus current = issue.getStatus();

        // 🔥 Workflow validation
        if (current == IssueStatus.OPEN && status == IssueStatus.DONE) {
            throw new InvalidStatusTransitionException("Cannot move OPEN → DONE directly");
        }

        if (current == IssueStatus.DONE) {
            throw new InvalidStatusTransitionException("Issue already completed");
        }

        if (current == IssueStatus.IN_PROGRESS && status == IssueStatus.OPEN) {
            throw new InvalidStatusTransitionException("Cannot move back to OPEN");
        }

        issue.setStatus(status);

        return issueRepository.save(issue);
    }

    // ✅ Get Issues by Project (Paginated)
    @Override
    public Page<Issue> getProjectIssues(Long projectId, int page, int size) {

        // Optional validation (good practice)
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException("Project not found");
        }

        return issueRepository.findByProjectId(projectId, PageRequest.of(page, size));
    }

    // ✅ Get My Issues (Paginated)
    @Override
    public Page<Issue> getMyIssues(Long userId, int page, int size) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }

        return issueRepository.findByAssigneeId(userId, PageRequest.of(page, size));
    }
}