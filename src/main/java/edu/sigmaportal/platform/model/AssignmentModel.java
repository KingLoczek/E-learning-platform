package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("Assignments")
public record AssignmentModel(
        @Id
        int assignmentId,
        String name,
        String content,
        OffsetDateTime dueDate,
        OffsetDateTime closeDate,
        int assignedBy,
        int topicId
) {
}
