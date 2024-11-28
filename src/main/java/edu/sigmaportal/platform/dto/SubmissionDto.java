package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(name = "Submission")
public class SubmissionDto {
    @Schema(example = "123", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "456")
    @JsonProperty("student")
    public String studentId;

    @Schema(example = "789")
    @JsonProperty("assignment")
    public String assignmentId;

    @ArraySchema(arraySchema = @Schema(example = "[\"980\", \"67\"]", accessMode = Schema.AccessMode.WRITE_ONLY))
    @JsonProperty("files")
    public List<String> fileIds;

    @Schema(example = "1941-09-09T21:37:00.000Z")
    @JsonProperty("submitted_at")
    public Instant submittedAt;

    @Schema(example = "true")
    public boolean submitted;
}
