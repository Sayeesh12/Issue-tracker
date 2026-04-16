package com.issuetracker.controller;

import com.issuetracker.entity.Project;
import com.issuetracker.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public Project createProject(@RequestBody Project project, Authentication auth) {

        Long userId = getUserId(auth);

        return projectService.createProject(project, userId);
    }

    @GetMapping
    public List<Project> getProjects(Authentication auth) {
        Long userId = getUserId(auth);
        return projectService.getUserProjects(userId);
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.getProject(id);
    }

    // 🔥 helper (temporary until full user context mapping)
    private Long getUserId(Authentication auth) {
        // You will replace this later with proper user lookup
        return 1L;
    }
}