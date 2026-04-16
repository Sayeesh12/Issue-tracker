package com.issuetracker.service.impl;

import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.entity.Comment;
import com.issuetracker.entity.Issue;
import com.issuetracker.entity.User;
import com.issuetracker.exception.IssueNotFoundException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.repository.CommentRepository;
import com.issuetracker.repository.IssueRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    // ✅ Add Comment
    @Override
    public CommentResponse addComment(Long issueId, Long userId, String content) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setIssue(issue);
        comment.setAuthor(user);

        Comment saved = commentRepository.save(comment);

        return mapToResponse(saved);
    }

    // ✅ Get Comments
    @Override
    public List<CommentResponse> getComments(Long issueId) {

        List<Comment> comments = commentRepository.findByIssueId(issueId);

        return comments.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔁 Mapping
    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .issueId(comment.getIssue().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}