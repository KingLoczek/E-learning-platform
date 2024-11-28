package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.sigmaportal.platform.model.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Course")
public class CourseDto {
    @Schema(example = "3756", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "Data Bases")
    public String name;

    @Schema(example = "Course about data bases")
    public String description;

    @Schema(example = "5674")
    @JsonProperty("instructor_id")
    public String instructorId;

    @Schema(example = "hohoho", accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty("access_key")
    public String accessKey;

    @Schema(enumAsRef = true, name = "status_type", example = "ARCHIVED")
    @JsonProperty("status_type")
    public StatusType status;
}
