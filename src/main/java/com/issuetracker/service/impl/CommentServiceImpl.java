package com.issuetracker.service.impl;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.entity.Comment;
import com.issuetracker.entity.Issue;
import com.issuetracker.entity.User;
import com.issuetracker.exception.CommentNotFoundException;
import com.issuetracker.exception.IssueNotFoundException;
import com.issuetracker.mapper.CommentMapper;
import com.issuetracker.repository.CommentRepository;
import com.issuetracker.repository.IssueRepository;
import com.issuetracker.service.auth.AuthorizationService;
import com.issuetracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final CommentMapper commentMapper;
    private final AuthorizationService authorizationService;

    // ---------------- ADD COMMENT ----------------
    @Override
    public CommentResponse addComment(Long issueId, AddCommentRequest request, User user) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        authorizationService.canComment(user, issue);

        Comment comment = commentMapper.toEntity(request);
        comment.setUser(user);
        comment.setIssue(issue);

        return commentMapper.toResponse(commentRepository.save(comment));
    }

    // ---------------- GET COMMENTS ----------------
    @Override
    public List<CommentResponse> getCommentsByIssue(Long issueId, User user) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        authorizationService.canViewIssue(user, issue);

        return commentRepository.findByIssueIdAndDeletedFalse(issueId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    // ---------------- DELETE COMMENT (SOFT DELETE) ----------------
    @Override
    public void deleteComment(Long commentId, User user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));

        authorizationService.canDeleteComment(user, comment);

        comment.setDeleted(true);
        commentRepository.save(comment);
    }
}