package com.issuetracker.service;

import com.issuetracker.dto.request.CreateProjectRequest;
import com.issuetracker.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(CreateProjectRequest request, Long userId);

    ProjectResponse getProjectById(Long projectId);

    List<ProjectResponse> getUserProjects(Long userId);
}