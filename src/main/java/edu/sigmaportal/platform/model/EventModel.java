package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("Events")
public record EventModel(
        @Id
        int eventId,
        String title,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        int courseId
) {
}
