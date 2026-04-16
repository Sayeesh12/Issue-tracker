package com.issuetracker.service.impl;

import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import com.issuetracker.exception.ProjectNotFoundException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.repository.ProjectRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    @Override
    public ProjectResponse createProject(String name, String description, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setOwner(user);
        project.getMembers().add(user); // owner = member

        Project saved = projectRepository.save(project);

        return mapToResponse(saved);
    }


    @Override
    public List<ProjectResponse> getUserProjects(Long userId) {

        return projectRepository.findByMembers_Id(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public ProjectResponse getProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        return mapToResponse(project);
    }


    private ProjectResponse mapToResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .build();
    }
}