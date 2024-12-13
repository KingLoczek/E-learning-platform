package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "Assignment")
public class AssignmentDto {
    @Schema(example = "4387", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "Fibonacci number sequence")
    @NotEmpty
    @Size(max = 100)
    public String name;

    @Schema(example = "Write a simple C++ program that computes _n_th Fibonacci number")
    public String content;

    @ArraySchema(arraySchema = @Schema(example = "[\"230\", \"448\", \"9\"]", name = "files"))
    @JsonProperty("files")
    public List<String> fileIds;

    @Schema(name = "due_date", example = "1991-01-01T01:01:00.000Z")
    @JsonProperty("due_date")
    public OffsetDateTime dueDate;

    @Schema(name = "close_date", example = "1991-01-01T01:01:00.000Z")
    @JsonProperty("close_date")
    public OffsetDateTime closeDate;

    @Schema(name = "assigned_by", example = "2341", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("assigned_by")
    public String assigner;

    @Schema(name = "topic_id", example = "602")
    @JsonProperty("topic_id")
    @NotEmpty
    public String topicId;

    public AssignmentDto(String id, String name, String content, List<String> fileIds, OffsetDateTime dueDate, OffsetDateTime closeDate, String assigner, String topicId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.fileIds = fileIds;
        this.dueDate = dueDate;
        this.closeDate = closeDate;
        this.assigner = assigner;
        this.topicId = topicId;
    }
}
