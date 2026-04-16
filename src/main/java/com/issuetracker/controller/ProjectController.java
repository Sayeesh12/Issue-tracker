package com.issuetracker.controller;

import com.issuetracker.dto.request.CreateProjectRequest;
import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;


    @PostMapping
    public ProjectResponse createProject(
            @Valid @RequestBody CreateProjectRequest request,
            Authentication auth
    ) {
        Long userId = getUserId(auth);

        return projectService.createProject(
                request.getName(),
                request.getDescription(),
                userId
        );
    }


    @GetMapping
    public List<ProjectResponse> getProjects(Authentication auth) {
        Long userId = getUserId(auth);
        return projectService.getUserProjects(userId);
    }


    @GetMapping("/{id}")
    public ProjectResponse getProject(@PathVariable Long id) {
        return projectService.getProject(id);
    }


    private Long getUserId(Authentication auth) {
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getId();
    }
}