package com.issuetracker.service.impl;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.request.UpdateCommentRequest;
import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.entity.Comment;
import com.issuetracker.entity.Issue;
import com.issuetracker.entity.User;
import com.issuetracker.exception.IssueNotFoundException;
import com.issuetracker.exception.UserNotFoundException;
import com.issuetracker.exception.UnauthorizedActionException;
import com.issuetracker.mapper.CommentMapper;
import com.issuetracker.repository.CommentRepository;
import com.issuetracker.repository.IssueRepository;
import com.issuetracker.repository.UserRepository;
import com.issuetracker.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse addComment(AddCommentRequest request, Long userId) {

        Issue issue = issueRepository.findById(request.getIssueId())
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentMapper.toEntity(request);

        comment.setIssue(issue);
        comment.setAuthor(user);

        Comment saved = commentRepository.save(comment);

        return commentMapper.toResponse(saved);
    }

    @Override
    public List<CommentResponse> getCommentsByIssue(Long issueId) {

        return commentRepository.findByIssueId(issueId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @Override
    public CommentResponse updateComment(Long commentId, UpdateCommentRequest request, Long userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // ✅ Only author can edit
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new UnauthorizedActionException("You can only edit your own comment");
        }

        // ✅ mapper update
        commentMapper.updateEntityFromDto(request, comment);

        Comment updated = commentRepository.save(comment);

        return commentMapper.toResponse(updated);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // ✅ Only author can delete
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new UnauthorizedActionException("You can only delete your own comment");
        }

        commentRepository.delete(comment);
    }
}