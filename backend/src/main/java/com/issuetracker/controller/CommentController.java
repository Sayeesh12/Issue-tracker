package com.issuetracker.controller;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.entity.User;
import com.issuetracker.service.CommentService;
import com.issuetracker.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;

    // ---------------- ADD COMMENT ----------------
    @PostMapping("/issues/{issueId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long issueId,
            @Valid @RequestBody AddCommentRequest request,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                commentService.addComment(issueId, request, user)
        );
    }

    // ---------------- GET COMMENTS ----------------
    @GetMapping("/issues/{issueId}")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long issueId,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        return ResponseEntity.ok(
                commentService.getCommentsByIssue(issueId, user)
        );
    }

    // ---------------- DELETE COMMENT ----------------
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        User user = authService.getCurrentUser(authentication);

        commentService.deleteComment(commentId, user);

        return ResponseEntity.noContent().build();
    }
}