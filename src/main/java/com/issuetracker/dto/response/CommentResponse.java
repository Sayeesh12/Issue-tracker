package com.issuetracker.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {

    private Long id;
    private String content;

    private Long issueId;

    private Long authorId;
    private String authorName;

    private LocalDateTime createdAt;
}