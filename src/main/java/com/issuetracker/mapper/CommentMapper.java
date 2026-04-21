package com.issuetracker.mapper;

import com.issuetracker.dto.request.AddCommentRequest;
import com.issuetracker.dto.request.UpdateCommentRequest;
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

        // ❌ DO NOT set author or issue here
        return comment;
    }

    // Entity → Response
    public CommentResponse toResponse(Comment comment) {
        if (comment == null) return null;

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .issueId(comment.getIssue().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public void updateEntityFromDto(UpdateCommentRequest request, Comment comment) {
        if (request == null || comment == null) return;

        if (request.getContent() != null) {
            comment.setContent(request.getContent());
        }
    }
}