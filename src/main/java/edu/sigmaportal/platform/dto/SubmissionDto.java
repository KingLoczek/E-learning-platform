package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "Submission")
public class SubmissionDto {
    @Schema(example = "123", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "456", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("student")
    public String studentId;

    @Schema(example = "789")
    @JsonProperty("assignment")
    @NotBlank
    public String assignmentId;

    @ArraySchema(arraySchema = @Schema(example = "[\"980\", \"67\"]", accessMode = Schema.AccessMode.WRITE_ONLY))
    @JsonProperty("files")
    @Size(min = 1)
    public List<String> fileIds;

    @Schema(example = "1941-09-09T21:37:00.000Z", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("submitted_at")
    public OffsetDateTime submittedAt;

    @Schema(example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    public Boolean submitted;

    public SubmissionDto(String id, String studentId, String assignmentId, List<String> fileIds, OffsetDateTime submittedAt, Boolean submitted) {
        this.id = id;
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.fileIds = fileIds;
        this.submittedAt = submittedAt;
        this.submitted = submitted;
    }
}
