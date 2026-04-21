package com.issuetracker.service.impl;

import com.issuetracker.dto.request.CreateProjectRequest;
import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import com.issuetracker.exception.ProjectNotFoundException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.mapper.ProjectMapper;
import com.issuetracker.repository.ProjectRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectResponse createProject(CreateProjectRequest request, Long userId) {

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Project project = projectMapper.toEntity(request);
        project.setOwner(creator);
        // ✅ creator is always a member
        HashSet<User> members = new HashSet<>();
        members.add(creator);

        // ✅ add additional members (if provided)
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            List<User> users = userRepository.findAllById(request.getMemberIds());
            members.addAll(users);
        }

        project.setMembers(members);

        Project saved = projectRepository.save(project);

        return projectMapper.toResponse(saved);
    }

    @Override
    public ProjectResponse getProjectById(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        return projectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> getUserProjects(Long userId) {

        List<Project> projects = projectRepository.findByMembers_Id(userId);

        return projects.stream()
                .map(projectMapper::toResponse)
                .toList();
    }
}