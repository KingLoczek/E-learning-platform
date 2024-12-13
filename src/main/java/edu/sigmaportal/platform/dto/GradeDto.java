package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.OffsetDateTime;

@Schema(name = "Grade")
public class GradeDto {
    @Schema(example = "838", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "554")
    @JsonProperty("submission")
    public String submissionId;

    @Schema(example = "5")
    public Integer grade;

    @Schema(example = "Really good project!")
    public String feedback;

    @Schema(example = "1943-02-04T21:37:00.000Z", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("graded_at")
    public OffsetDateTime gradedAt;

    @Schema(example = "321")
    public String instructorId;

    public GradeDto(String id, String submissionId, int grade, String feedback, OffsetDateTime gradedAt, String instructorId) {
        this.id = id;
        this.submissionId = submissionId;
        this.grade = grade;
        this.feedback = feedback;
        this.gradedAt = gradedAt;
        this.instructorId = instructorId;
    }
}
