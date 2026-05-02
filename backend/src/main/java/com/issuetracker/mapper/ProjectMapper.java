package com.issuetracker.mapper;

import com.issuetracker.dto.request.ProjectRequest;
import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    // DTO → Entity (basic fields only)
    public Project toEntity(ProjectRequest request) {
        if (request == null) return null;

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return project;
    }

    // Entity → DTO
    public ProjectResponse toResponse(Project project) {
        if (project == null) return null;

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .memberIds(
                        project.getMembers() != null
                                ? project.getMembers().stream()
                                  .map(User::getId)
                                  .collect(Collectors.toList())
                                : List.of()
                )
                .build();
    }
}