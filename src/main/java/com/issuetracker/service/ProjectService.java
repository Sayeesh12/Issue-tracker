package com.issuetracker.service;

import com.issuetracker.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(String name, String description, Long userId);

    List<ProjectResponse> getUserProjects(Long userId);

    ProjectResponse getProject(Long projectId);
}