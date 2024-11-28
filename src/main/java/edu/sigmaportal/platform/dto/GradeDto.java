package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "Grade")
public class GradeDto {
    @Schema(example = "838", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "554")
    @JsonProperty("submission")
    public String submissionId;

    @Schema(example = "5.5")
    public float grade;

    @Schema(example = "Really good project!")
    public String feedback;

    @Schema(example = "1943-02-04T21:37:00.000Z")
    @JsonProperty("graded_at")
    public Instant gradedAt;
}
