package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user")
@Tag(name = "user", description = "the user API")
public class UsersController {

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "List all users")
    public Collection<UserDto> allUsers() {
        return Collections.emptyList();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public UserDto findById(@Parameter(description = "ID of use to find") @PathVariable("id") String id) {
        return null;
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Something was wrong with the user object", content = @Content())
    })
    public UserDto create(@RequestBody UserDto user) {
        return null;
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Edit user details",
            description = "Method that allows changing user details such as: first/last name, email, etc. However, it cannot be used to change ID or password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User edited", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Something was wrong with the user object", content = @Content()),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content()),
    })
    public UserDto update(@Parameter(description = "ID of user to edit") @PathVariable("id") String id, @RequestBody UserDto user) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Only current logged in user can delete his account. Administrator can delete any account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void delete(@Parameter(description = "ID of user to delete") @PathVariable("id") String id) {
    }
}
