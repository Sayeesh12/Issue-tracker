package com.issuetracker.service.impl;

import com.issuetracker.dto.request.CreateIssueRequest;
import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.entity.Issue;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.exception.*;
import com.issuetracker.mapper.IssueMapper;
import com.issuetracker.repository.IssueRepository;
import com.issuetracker.repository.ProjectRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final IssueMapper issueMapper;

    @Override
    public IssueResponse createIssue(CreateIssueRequest request, Long userId) {

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        boolean isMember = projectRepository
                .existsByIdAndMembers_Id(request.getProjectId(), userId);

        if (!isMember) {
            throw new UnauthorizedActionException("User not part of project");
        }

        // ✅ use mapper
        Issue issue = issueMapper.toEntity(request);

        // ✅ relationships (service responsibility)
        issue.setCreator(creator);
        issue.setProject(project);

        Issue saved = issueRepository.save(issue);   // ✅ REQUIRED

        return issueMapper.toResponse(saved);
    }

    @Override
    public IssueResponse getIssueById(Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        return issueMapper.toResponse(issue);
    }

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

        return issueMapper.toResponse(issueRepository.save(issue));
    }

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

        return issueMapper.toResponse(issueRepository.save(issue));
    }

    @Override
    public Page<IssueResponse> getProjectIssues(Long projectId, int page, int size) {

        return issueRepository.findByProjectId(projectId, PageRequest.of(page, size))
                .map(issueMapper::toResponse);
    }

    @Override
    public Page<IssueResponse> getMyIssues(Long userId, int page, int size) {

        return issueRepository.findByAssigneeId(userId, PageRequest.of(page, size))
                .map(issueMapper::toResponse);
    }

}