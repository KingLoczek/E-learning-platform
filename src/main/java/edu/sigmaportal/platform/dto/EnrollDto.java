package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(name = "Enroll", description = "helper object for processing enrolling into a course")
public class EnrollDto {
    @Schema(example = "hohoho")
    @JsonProperty("access_key")
    @Size(max = 10, message = "access_key is too long, max 10 characters allowed")
    public String accessKey;
}
