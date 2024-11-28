package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "Assignment")
public class AssignmentDto {
    @Schema(example = "4387")
    public String id;

    @Schema(example = "Fibonacci number sequence")
    public String name;

    @Schema(example = "Write a simple C++ program that computes _n_th Fibonacci number")
    public String content;

    @Schema(name = "due_date", example = "1991-01-01T01:01:00.000Z")
    @JsonProperty("due_date")
    public Instant dueDate;

    @Schema(name = "assigned_by", example = "2341")
    @JsonProperty("assigned_by")
    public String assigner;

    @Schema(name = "topic_id", example = "602")
    @JsonProperty("topic_id")
    public String topicId;
}
