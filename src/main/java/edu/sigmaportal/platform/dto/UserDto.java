package edu.sigmaportal.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Schema(name = "User")
public class UserDto {
    @Schema(example = "3416", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(name = "first_name", example = "Mariusz")
    @JsonProperty("first_name")
    @NotEmpty @Size(max = 50)
    public String firstName;

    @Schema(name = "last_name", example = "Rudy")
    @JsonProperty("last_name")
    @NotEmpty @Size(max = 50)
    public String lastName;

    @Schema(example = "marrud2137@student.pwr.edu.pl")
    @NotEmpty @Email @Size(max = 100)
    public String email;

    @Schema(example = "password123", accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotEmpty @Size(max = 255)
    public String password;

    @Schema(name = "user_type", example = "student")
    @JsonProperty("user_type")
    public String userType;

    public UserDto(String id, String firstName, String lastName, String email, String password, String userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
}
