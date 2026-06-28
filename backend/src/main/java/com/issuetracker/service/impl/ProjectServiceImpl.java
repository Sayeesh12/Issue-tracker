package com.issuetracker.service.impl;

import com.issuetracker.dto.request.ProjectRequest;
import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import com.issuetracker.exception.ProjectNotFoundException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.mapper.ProjectMapper;
import com.issuetracker.repository.ProjectRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.ProjectService;
import com.issuetracker.service.auth.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final AuthorizationService authorizationService;

    @Override
    public ProjectResponse createProject(ProjectRequest request, User user) {

        Project project = projectMapper.toEntity(request);
        project.setOwner(user);
        project.getMembers().add(user);

        if (request.getMemberIds() != null) {
            request.getMemberIds().stream()
                    .filter(memberId -> !memberId.equals(user.getId()))
                    .forEach(memberId ->
                            userRepository.findById(memberId)
                                    .ifPresent(project.getMembers()::add)
                    );
        }

        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public ProjectResponse getProjectById(Long projectId, User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        authorizationService.canViewProject(user, project);

        return projectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> getUserProjects(User user) {
        return projectRepository.findByMembersContaining(user)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest request, User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        authorizationService.canUpdateProject(user, project);

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return projectMapper.toResponse(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long projectId, User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        authorizationService.canDeleteProject(user, project);

        projectRepository.delete(project);
    }

    @Override
    public ProjectResponse addMember(Long projectId, String email, User user) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        authorizationService.canUpdateProject(user, project);

        User member = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UserNotFoundException("No user found with that email"));

        project.getMembers().add(member);

        return projectMapper.toResponse(projectRepository.save(project));
    }
}
