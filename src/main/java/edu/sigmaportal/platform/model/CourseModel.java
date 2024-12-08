package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Courses")
public record CourseModel(
        @Id
        int courseId,
        String name,
        String description,
        String accessKey,
        int instructorId,
        @Column("status_type")
        StatusType status
) {
}
