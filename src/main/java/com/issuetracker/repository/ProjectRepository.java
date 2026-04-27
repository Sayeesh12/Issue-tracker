package com.issuetracker.repository;

import com.issuetracker.entity.Project;
import com.issuetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByMembersContaining(User user);
}