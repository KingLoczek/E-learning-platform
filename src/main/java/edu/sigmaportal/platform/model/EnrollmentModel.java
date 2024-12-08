package edu.sigmaportal.platform.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("Enrollments")
public record EnrollmentModel(
        @Id
        int enrollmentId,
        int studentId,
        int courseId,
        OffsetDateTime joinedAt,
        @Column("status_type")
        StatusType status
) {
}
