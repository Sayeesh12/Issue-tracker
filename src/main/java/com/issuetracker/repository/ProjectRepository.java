package com.issuetracker.repository;

import com.issuetracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    // 🔥 Required for DTO-based service
    List<Project> findByMembers_Id(Long userId);

    // 🔥 Required for validation
    boolean existsByIdAndMembers_Id(Long projectId, Long userId);
}