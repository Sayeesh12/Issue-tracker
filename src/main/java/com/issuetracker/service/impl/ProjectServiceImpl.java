package com.issuetracker.service.impl;

import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import com.issuetracker.exception.ProjectNotFoundException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.repository.ProjectRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public Project createProject(Project project, Long userId) {

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        project.setOwner(owner);

        // add owner to members
        project.setMembers(new HashSet<>());
        project.getMembers().add(owner);

        return projectRepository.save(project);
    }

    @Override
    public List<Project> getUserProjects(Long userId) {
        return projectRepository.findByOwnerId(userId);
    }

    @Override
    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
    }
}