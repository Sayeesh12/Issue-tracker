package com.issuetracker.service;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.request.UpdateCommentRequest;
import com.issuetracker.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse addComment(AddCommentRequest request, Long userId);

    List<CommentResponse> getCommentsByIssue(Long issueId);

    CommentResponse updateComment(Long commentId, UpdateCommentRequest request, Long userId);

    void deleteComment(Long commentId, Long userId);
}