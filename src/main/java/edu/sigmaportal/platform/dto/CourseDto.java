package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.sigmaportal.platform.model.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Schema(name = "Course")
public class CourseDto {
    @Schema(example = "3756", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "Data Bases")
    @NotEmpty @Size(max = 100)
    public String name;

    @Schema(example = "Course about data bases")
    public String description;

    @Schema(example = "5674")
    @JsonProperty("instructor_id")
    public String instructorId;

    @Schema(example = "hohoho", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty("access_key")
    @Size(max = 10)
    public String accessKey;

    @Schema(enumAsRef = true, name = "status_type", example = "ARCHIVED")
    @JsonProperty("status_type")
    public StatusType status;

    public CourseDto(String id, String name, String description, String instructorId, String accessKey, StatusType status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.instructorId = instructorId;
        this.accessKey = accessKey;
        this.status = status;
    }
}
