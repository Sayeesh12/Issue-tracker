package com.issuetracker.service;

import com.issuetracker.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment addComment(Long issueId, Long userId, String content);

    List<Comment> getComments(Long issueId);
}