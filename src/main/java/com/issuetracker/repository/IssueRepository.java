package com.issuetracker.repository;

import com.issuetracker.entity.Issue;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.enums.IssueType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    //  Core: paginated project issues (use this instead of List version)
    Page<Issue> findByProjectId(Long projectId, Pageable pageable);

    // My Issues
    Page<Issue> findByAssigneeId(Long assigneeId, Pageable pageable);

    // Created by user
    Page<Issue> findByCreatorId(Long creatorId, Pageable pageable);

    // Filtering
    Page<Issue> findByProjectIdAndStatus(Long projectId, IssueStatus status, Pageable pageable);

    Page<Issue> findByProjectIdAndType(Long projectId, IssueType type, Pageable pageable);

    Page<Issue> findByProjectIdAndAssigneeId(Long projectId, Long assigneeId, Pageable pageable);
}