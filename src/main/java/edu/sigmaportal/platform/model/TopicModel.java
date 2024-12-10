package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Topics")
public record TopicModel(
        @Id
        int topicId,
        String title,
        String description,
        int courseId
) {
}
