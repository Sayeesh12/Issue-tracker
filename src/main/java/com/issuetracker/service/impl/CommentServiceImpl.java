package com.issuetracker.service.impl;

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

    @Override
    public Comment addComment(Long issueId, Long userId, String content) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException("Issue not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .content(content)
                .issue(issue)
                .author(user)
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(Long issueId) {
        return commentRepository.findByIssueId(issueId);
    }
}