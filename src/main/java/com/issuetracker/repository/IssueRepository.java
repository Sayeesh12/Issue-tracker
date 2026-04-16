package com.issuetracker.repository;

import com.issuetracker.entity.Issue;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.enums.IssueType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {


    Page<Issue> findByProjectId(Long projectId, Pageable pageable);


    Page<Issue> findByAssigneeId(Long assigneeId, Pageable pageable);


    Page<Issue> findByCreatorId(Long creatorId, Pageable pageable);


    Page<Issue> findByProjectIdAndStatus(Long projectId, IssueStatus status, Pageable pageable);

    Page<Issue> findByProjectIdAndType(Long projectId, IssueType type, Pageable pageable);

    Page<Issue> findByProjectIdAndAssigneeId(Long projectId, Long assigneeId, Pageable pageable);
}