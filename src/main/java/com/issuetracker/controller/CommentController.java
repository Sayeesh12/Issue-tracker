package com.issuetracker.controller;

import com.issuetracker.entity.Comment;
import com.issuetracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public Comment addComment(
            @RequestParam Long issueId,
            @RequestParam String content,
            Authentication auth
    ) {
        Long userId = getUserId(auth);
        return commentService.addComment(issueId, userId, content);
    }

    @GetMapping("/{issueId}")
    public List<Comment> getComments(@PathVariable Long issueId) {
        return commentService.getComments(issueId);
    }

    private Long getUserId(Authentication auth) {
        return 1L; // replace later
    }
}