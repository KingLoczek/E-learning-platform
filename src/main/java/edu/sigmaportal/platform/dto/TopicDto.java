package edu.sigmaportal.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "Topic")
public class TopicDto {
    @Schema(example = "123", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "Setting up PostreSQL")
    @NotEmpty @Size(max = 100)
    public String title;

    @Schema(example = "How to setup PostreSQL on Fedora", nullable = true)
    public String description;

    @Schema(example = "564")
    @NotEmpty
    public String courseId;

    public TopicDto(String id, String title, String description, String courseId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.courseId = courseId;
    }
}
