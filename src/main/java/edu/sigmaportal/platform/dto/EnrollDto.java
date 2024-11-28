package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Enroll", description = "helper object for processing enrolling into a course")
public class EnrollDto {
    @Schema(example = "hohoho")
    @JsonProperty("access_key")
    public String accessKey;
}
