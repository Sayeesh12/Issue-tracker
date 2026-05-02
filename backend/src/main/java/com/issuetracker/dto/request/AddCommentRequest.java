package com.issuetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCommentRequest {

    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 1000, message = "Comment too long")
    private String content;
}