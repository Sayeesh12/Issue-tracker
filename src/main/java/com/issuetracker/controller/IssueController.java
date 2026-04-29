package com.issuetracker.controller;

import com.issuetracker.dto.request.CreateIssueRequest;
import com.issuetracker.dto.response.IssueActivityResponse;
import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.entity.User;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.service.IssueService;
import com.issuetracker.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final AuthService authService;

    // ---------------- CREATE ISSUE ----------------
    @PostMapping("/projects/{projectId}")
    public ResponseEntity<IssueResponse> createIssue(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateIssueRequest request,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                issueService.createIssue(projectId, request, user)
        );
    }

    // ---------------- GET SINGLE ISSUE ----------------
    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponse> getIssue(
            @PathVariable Long issueId,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                issueService.getIssueById(issueId, user)
        );
    }

    // ---------------- ASSIGN ISSUE ----------------
    @PatchMapping("/{issueId}/assign")
    public ResponseEntity<IssueResponse> assignIssue(
            @PathVariable Long issueId,
            @RequestParam Long assigneeId,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                issueService.assignIssue(issueId, assigneeId, user)
        );
    }

    // ---------------- UPDATE STATUS ----------------
    @PatchMapping("/{issueId}/status")
    public ResponseEntity<IssueResponse> updateStatus(
            @PathVariable Long issueId,
            @RequestParam IssueStatus status,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                issueService.updateStatus(issueId, status, user)
        );
    }

    // ---------------- GET PROJECT ISSUES ----------------
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<Page<IssueResponse>> getProjectIssues(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                issueService.getProjectIssues(projectId, page, size, user)
        );
    }

    // ---------------- GET MY ISSUES ----------------
    @GetMapping("/my")
    public ResponseEntity<Page<IssueResponse>> getMyIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                issueService.getMyIssues(user, page, size)
        );
    }

    // ---------------- GET ISSUE ACTIVITIES ----------------
    @GetMapping("/{issueId}/activities")
    public ResponseEntity<List<IssueActivityResponse>> getIssueActivities(
            @PathVariable Long issueId,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                issueService.getIssueActivities(issueId, user)
        );
    }
}