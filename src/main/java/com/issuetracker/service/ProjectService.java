package com.issuetracker.service;

import com.issuetracker.entity.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project, Long userId);

    List<Project> getUserProjects(Long userId);

    Project getProject(Long projectId);
}