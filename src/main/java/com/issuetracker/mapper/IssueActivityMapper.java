package com.issuetracker.mapper;

import com.issuetracker.dto.response.IssueActivityResponse;
import com.issuetracker.entity.IssueActivity;
import org.springframework.stereotype.Component;

@Component
public class IssueActivityMapper {

    public IssueActivityResponse toResponse(IssueActivity activity) {
        return IssueActivityResponse.builder()
                .action(activity.getAction())
                .performedBy(activity.getPerformedBy().getEmail())
                .details(activity.getDetails())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}