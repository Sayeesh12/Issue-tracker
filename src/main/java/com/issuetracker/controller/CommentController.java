package com.issuetracker.controller;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.CommentService;
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
    private final UserRepository userRepository;

    // ✅ Add Comment
    @PostMapping
    public CommentResponse addComment(
            @Valid @RequestBody AddCommentRequest request,
            Authentication auth
    ) {
        Long userId = getUserId(auth);

        return commentService.addComment(
                request.getIssueId(),
                userId,
                request.getContent()
        );
    }

    // ✅ Get Comments
    @GetMapping("/{issueId}")
    public List<CommentResponse> getComments(@PathVariable Long issueId) {
        return commentService.getComments(issueId);
    }

    // 🔥 JWT → userId
    private Long getUserId(Authentication auth) {
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                .getId();
    }
}