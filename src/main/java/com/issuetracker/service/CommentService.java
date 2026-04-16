package com.issuetracker.service;

import com.issuetracker.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse addComment(Long issueId, Long userId, String content);

    List<CommentResponse> getComments(Long issueId);
}