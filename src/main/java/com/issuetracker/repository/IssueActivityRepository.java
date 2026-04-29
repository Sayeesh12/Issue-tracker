package com.issuetracker.repository;

import com.issuetracker.entity.Issue;
import com.issuetracker.entity.IssueActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueActivityRepository extends JpaRepository<IssueActivity, Long> {

    List<IssueActivity> findByIssueOrderByCreatedAtDesc(Issue issue);
}
