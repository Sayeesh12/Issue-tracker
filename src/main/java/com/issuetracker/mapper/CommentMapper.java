package com.issuetracker.mapper;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.response.CommentResponse;
import com.issuetracker.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    // DTO → Entity (ONLY direct fields)
    public Comment toEntity(AddCommentRequest request) {
        if (request == null) return null;

        Comment comment = new Comment();
        comment.setContent(request.getContent());

        // ❌ DO NOT set user or issue here (handled in service)
        return comment;
    }

    // Entity → Response
    public CommentResponse toResponse(Comment comment) {
        if (comment == null) return null;

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .issueId(comment.getIssue().getId())
                .authorId(comment.getUser().getId())          // ✅ FIXED
                .authorName(comment.getUser().getName())      // ✅ FIXED
                .createdAt(comment.getCreatedAt())
                .build();
    }
}