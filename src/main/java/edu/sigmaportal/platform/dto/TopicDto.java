package edu.sigmaportal.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Topic")
public class TopicDto {
    @Schema(example = "123", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "Setting up PostreSQL")
    public String title;

    @Schema(example = "How to setup PostreSQL on Fedora", nullable = true)
    public String description;

    @Schema(example = "564")
    public String courseId;
}
