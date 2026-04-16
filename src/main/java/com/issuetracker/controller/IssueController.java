package com.issuetracker.controller;

import com.issuetracker.entity.Issue;
import com.issuetracker.enums.IssueStatus;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.IssueService;
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
    private final UserRepository userRepository; // 🔥 REQUIRED

    // ✅ Create Issue
    @PostMapping
    public Issue createIssue(
            @Valid @RequestBody Issue issue,
            @RequestParam Long projectId,
            Authentication auth
    ) {
        Long userId = getUserId(auth);
        return issueService.createIssue(issue, userId, projectId);
    }

    // ✅ Assign Issue
    @PatchMapping("/{id}/assign")
    public Issue assignIssue(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return issueService.assignIssue(id, userId);
    }

    // ✅ Update Status
    @PatchMapping("/{id}/status")
    public Issue updateStatus(
            @PathVariable Long id,
            @RequestParam IssueStatus status,
            Authentication auth
    ) {
        Long userId = getUserId(auth);
        return issueService.updateStatus(id, status, userId);
    }

    // ✅ Get Project Issues (Paginated)
    @GetMapping("/project/{projectId}")
    public Page<Issue> getProjectIssues(
            @PathVariable Long projectId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return issueService.getProjectIssues(projectId, page, size);
    }

    // ✅ Get My Issues
    @GetMapping("/my")
    public Page<Issue> getMyIssues(
            @RequestParam int page,
            @RequestParam int size,
            Authentication auth
    ) {
        Long userId = getUserId(auth);
        return issueService.getMyIssues(userId, page, size);
    }

    // 🔥 CORRECT USER EXTRACTION FROM JWT
    private Long getUserId(Authentication auth) {
        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getId();
    }
}