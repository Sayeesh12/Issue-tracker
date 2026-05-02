package com.issuetracker.controller;

import com.issuetracker.dto.request.ProjectRequest;
import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.entity.User;
import com.issuetracker.service.ProjectService;
import com.issuetracker.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final AuthService authService;

    // ---------------- CREATE PROJECT ----------------
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                projectService.createProject(request, user)
        );
    }

    // ---------------- GET PROJECT ----------------
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                projectService.getProjectById(id, user)
        );
    }

    // ---------------- GET USER PROJECTS ----------------
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getUserProjects(
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                projectService.getUserProjects(user)
        );
    }

    // ---------------- UPDATE PROJECT ----------------
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                projectService.updateProject(id, request, user)
        );
    }

    // ---------------- DELETE PROJECT ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        projectService.deleteProject(id, user);

        return ResponseEntity.noContent().build();
    }
}