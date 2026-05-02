package com.issuetracker.service;

import com.issuetracker.dto.request.ProjectRequest;
import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.entity.User;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest request, User user);

    ProjectResponse getProjectById(Long projectId, User user);

    List<ProjectResponse> getUserProjects(User user);

    ProjectResponse updateProject(Long projectId, ProjectRequest request, User user);

    void deleteProject(Long projectId, User user);
}