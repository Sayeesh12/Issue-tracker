package com.issuetracker.repository;

import com.issuetracker.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerId(Long ownerId);

    boolean existsByIdAndMembers_Id(Long projectId, Long userId);
}