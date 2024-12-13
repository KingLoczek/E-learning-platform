 package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("AssignmentFiles")
public record AssignmentFileModel(
        @Id
        int id,
        int assignmentId,
        int fileId
) {
}
