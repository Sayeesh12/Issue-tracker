package com.issuetracker.controller;

import com.issuetracker.dto.request.CreateIssueRequest;
import com.issuetracker.dto.response.IssueResponse;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.service.IssueService;
import com.issuetracker.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final AuthService authService;

    // ✅ Create Issue
    @PostMapping
    public IssueResponse createIssue(
            @Valid @RequestBody CreateIssueRequest request,
            Authentication auth
    ) {
        Long userId = authService.getCurrentUserId(auth);
        return issueService.createIssue(request, userId);
    }

    // ✅ Assign Issue
    @PatchMapping("/{id}/assign")
    public IssueResponse assignIssue(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return issueService.assignIssue(id, userId);
    }

    // ✅ Update Status
    @PatchMapping("/{id}/status")
    public IssueResponse updateStatus(
            @PathVariable Long id,
            @RequestParam IssueStatus status,
            Authentication auth
    ) {
        Long userId = authService.getCurrentUserId(auth);
        return issueService.updateStatus(id, status, userId);
    }

    // ✅ Get Project Issues
    @GetMapping("/project/{projectId}")
    public Page<IssueResponse> getProjectIssues(
            @PathVariable Long projectId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return issueService.getProjectIssues(projectId, page, size);
    }

    // ✅ Get My Issues
    @GetMapping("/my")
    public Page<IssueResponse> getMyIssues(
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth
    ) {
        Long userId = authService.getCurrentUserId(auth);
        return issueService.getMyIssues(userId, page, size);
    }

    // ✅ Get Single Issue
    @GetMapping("/{id}")
    public IssueResponse getIssue(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }
}