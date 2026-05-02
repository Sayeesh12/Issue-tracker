package com.issuetracker.service.auth;


import com.issuetracker.entity.*;

public interface AuthorizationService {

    void canViewProject(User user, Project project);

    void canUpdateProject(User user, Project project);

    void canDeleteProject(User user, Project project);

    void canCreateIssue(User user, Project project);

    void canViewIssue(User user, Issue issue);

    void canAssignIssue(User user, Issue issue);

    void canUpdateIssueStatus(User user, Issue issue);

    void canComment(User user, Issue issue);

    void canDeleteComment(User user, Comment comment);
}
