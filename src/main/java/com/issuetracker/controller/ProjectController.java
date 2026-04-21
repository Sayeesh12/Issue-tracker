package com.issuetracker.controller;

import com.issuetracker.dto.request.CreateProjectRequest;
import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.service.ProjectService;
import com.issuetracker.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final AuthService authService;

    @PostMapping
    public ProjectResponse createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication auth
    ) {
        Long userId = authService.getCurrentUserId(auth);
        return projectService.createProject(request, userId);
    }

    @GetMapping("/{id}")
    public ProjectResponse getProject(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/my")
    public List<ProjectResponse> getMyProjects(Authentication auth) {
        Long userId = authService.getCurrentUserId(auth);
        return projectService.getUserProjects(userId);
    }
}