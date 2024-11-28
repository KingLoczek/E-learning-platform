package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Material")
public class MaterialDto {
    @Schema(example = "409", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "The ISO C++ specification")
    public String name;

    @Schema(example = "ISO C++ spec in PDF format", nullable = true)
    public String content;

    @ArraySchema(arraySchema = @Schema(example = "[\"230\", \"448\", \"9\"]", name = "files", accessMode = Schema.AccessMode.WRITE_ONLY))
    @JsonProperty("files")
    public List<String> fileIds;

    @Schema(example = "522")
    @JsonProperty("topic_id")
    public String topicId;

    @Schema(example = "398", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("author")
    public String authorId;
}
