package com.issuetracker.controller;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.request.UpdateCommentRequest;
import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.service.CommentService;
import com.issuetracker.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;

    @PostMapping
    public CommentResponse addComment(
            @Valid @RequestBody AddCommentRequest request,
            Authentication auth
    ) {
        Long userId = authService.getCurrentUserId(auth);
        return commentService.addComment(request, userId);
    }

    @GetMapping("/issue/{issueId}")
    public List<CommentResponse> getComments(@PathVariable Long issueId) {
        return commentService.getCommentsByIssue(issueId);
    }

    @PutMapping("/{id}")
    public CommentResponse updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequest request,
            Authentication auth
    ) {
        Long userId = authService.getCurrentUserId(auth);
        return commentService.updateComment(id, request, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(
            @PathVariable Long id,
            Authentication auth
    ) {
        Long userId = authService.getCurrentUserId(auth);
        commentService.deleteComment(id, userId);
    }
}