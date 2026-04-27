package com.issuetracker.service.auth;


import com.issuetracker.entity.*;
import com.issuetracker.service.auth.AuthorizationService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public void canViewProject(User user, Project project) {
        if (!project.getMembers().contains(user)) {
            throw new AccessDeniedException("User is not part of the project");
        }
    }

    @Override
    public void canUpdateProject(User user, Project project) {
        if (!project.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only project owner can update");
        }
    }

    @Override
    public void canDeleteProject(User user, Project project) {
        if (!project.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only project owner can delete");
        }
    }

    @Override
    public void canCreateIssue(User user, Project project) {
        if (!project.getMembers().contains(user)) {
            throw new AccessDeniedException("User not allowed to create issue");
        }
    }

    @Override
    public void canViewIssue(User user, Issue issue) {
        if (!issue.getProject().getMembers().contains(user)) {
            throw new AccessDeniedException("User not allowed to view issue");
        }
    }

    @Override
    public void canAssignIssue(User user, Issue issue) {
        if (!issue.getProject().getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only owner can assign issues");
        }
    }

    @Override
    public void canUpdateIssueStatus(User user, Issue issue) {
        if (issue.getAssignee() == null ||
                !issue.getAssignee().getId().equals(user.getId())) {
            throw new AccessDeniedException("Only assignee can update status");
        }
    }

    @Override
    public void canComment(User user, Issue issue) {
        if (!issue.getProject().getMembers().contains(user)) {
            throw new AccessDeniedException("User not allowed to comment");
        }
    }

    @Override
    public void canDeleteComment(User user, Comment comment) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Can delete only own comment");
        }
    }
}