package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.sigmaportal.platform.model.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "Enrollment")
public class EnrollmentDto {
    @Schema(example = "342", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(name = "student_id", example = "3297")
    @JsonProperty("student_id")
    public String studentId;

    @Schema(name = "course_id", example = "4384")
    @JsonProperty("course_id")
    public String courseId;

    @Schema(name = "joined_at", example = "1911-06-16T21:37:00.000Z")
    @JsonProperty("joined_at")
    public Instant joinedAt;

    @Schema(enumAsRef = true, name = "status_type", example = "ONGOING")
    @JsonProperty("status_type")
    public StatusType status;
}
