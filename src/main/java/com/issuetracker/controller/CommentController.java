package com.issuetracker.controller;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.entity.User;
import com.issuetracker.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // ---------------- ADD COMMENT ----------------
    @PostMapping("/issues/{issueId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long issueId,
            @Valid @RequestBody AddCommentRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                commentService.addComment(issueId, request, user)
        );
    }

    // ---------------- GET COMMENTS ----------------
    @GetMapping("/issues/{issueId}")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long issueId,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                commentService.getCommentsByIssue(issueId, user)
        );
    }

    // ---------------- DELETE COMMENT ----------------
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user
    ) {
        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }
}