package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("Grades")
public record GradeModel(
        @Id
        int gradeId,
        int submissionId,
        int grade,
        String feedback,
        OffsetDateTime gradedAt,
        int instructorId
) {
}
