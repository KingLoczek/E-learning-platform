package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Materials")
public record MaterialModel(
        @Id
        int materialId,
        String name,
        String content,
        int authorId,
        int topicId
) {
}
