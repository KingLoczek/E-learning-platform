package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("Submissions")
public record SubmissionModel(
        @Id
        int submissionId,
        int studentId,
        int assignmentId,
        boolean isSubmitted,
        OffsetDateTime submittedAt
) {
}
