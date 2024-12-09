package edu.sigmaportal.platform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("FileTypes")
public record FileTypeModel(
        @Id
        int fileTypeId,
        String typeName,
        String mimeType,
        String extension
) {
}
