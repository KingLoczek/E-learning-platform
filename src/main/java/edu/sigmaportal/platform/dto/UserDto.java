package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "User")
public class UserDto {
    @Schema(example = "3416", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(name = "first_name", example = "Mariusz")
    @JsonProperty("first_name")
    public String firstName;

    @Schema(name = "last_name", example = "Rudy")
    @JsonProperty("last_name")
    public String lastName;

    @Schema(example = "marrud2137@student.pwr.edu.pl")
    public String email;

    @Schema(example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    public String password;

    @Schema(name = "user_type", example = "student")
    @JsonProperty("user_type")
    public String userType;
}
