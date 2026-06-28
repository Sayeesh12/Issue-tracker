package com.issuetracker.mapper;

import com.issuetracker.dto.request.ProjectRequest;
import com.issuetracker.dto.response.ProjectMemberResponse;
import com.issuetracker.dto.response.ProjectResponse;
import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectRequest request) {
        if (request == null) return null;

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return project;
    }

    public ProjectResponse toResponse(Project project) {
        if (project == null) return null;

        List<ProjectMemberResponse> members = project.getMembers() != null
                ? project.getMembers().stream()
                    .map(this::toMemberResponse)
                    .collect(Collectors.toList())
                : List.of();

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .memberIds(members.stream().map(ProjectMemberResponse::getId).collect(Collectors.toList()))
                .members(members)
                .build();
    }

    private ProjectMemberResponse toMemberResponse(User user) {
        return ProjectMemberResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
