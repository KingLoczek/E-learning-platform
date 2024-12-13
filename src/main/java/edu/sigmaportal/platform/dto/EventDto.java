package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.OffsetDateTime;

@Schema(name = "Event")
public class EventDto {
    @Schema(example = "876", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(name = "course_id", example = "4312")
    @JsonProperty("course_id")
    public String courseId;

    @Schema(example = "Quiz 1")
    public String title;

    @Schema(example = "1982-02-24T21:37:00.000Z")
    @JsonProperty("start_timestamp")
    public OffsetDateTime startDate;

    @Schema(example = "2010-01-27T21:37:00.000Z")
    @JsonProperty("end_timestamp")
    public OffsetDateTime endDate;

    public EventDto(String id, String courseId, String title, OffsetDateTime startDate, OffsetDateTime endDate) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
