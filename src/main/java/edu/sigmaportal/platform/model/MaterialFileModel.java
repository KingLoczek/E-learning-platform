package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("MaterialFiles")
public record MaterialFileModel(
        @Id
        int id,
        int materialId,
        int fileId
) {
}
