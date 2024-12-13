package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("SubmissionFiles")
public record SubmissionFileModel(
        @Id
        int id,
        int submissionId,
        int fileId
) {
}
