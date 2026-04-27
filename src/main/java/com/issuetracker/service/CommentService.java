package com.issuetracker.service;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.entity.User;

import java.util.List;

public interface CommentService {

    CommentResponse addComment(Long issueId, AddCommentRequest request, User user);

    List<CommentResponse> getCommentsByIssue(Long issueId, User user);

    void deleteComment(Long commentId, User user);
}