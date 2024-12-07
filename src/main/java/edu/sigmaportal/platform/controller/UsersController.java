package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.UserDto;
import edu.sigmaportal.platform.exception.InsufficientPermissionsException;
import edu.sigmaportal.platform.security.RoleAuthority;
import edu.sigmaportal.platform.security.UserWithId;
import edu.sigmaportal.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user")
@Tag(name = "user", description = "the user API")
public class UsersController {

    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "List all users")
    public Collection<UserDto> allUsers() {
        return service.users();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public UserDto findById(@Parameter(description = "ID of use to find") @PathVariable("id") String id) {
        return service.fetchUser(id);
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Something was wrong with the user object", content = @Content())
    })
    public UserDto create(@Valid @RequestBody UserDto user) {
        return service.createUser(user);
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Edit user details",
            description = "Method that allows changing user details such as: first/last name, email, etc. However, it cannot be used to change ID or password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User edited", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Something was wrong with the user object", content = @Content()),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content()),
    })
    public UserDto update(@Parameter(description = "ID of user to edit") @PathVariable("id") String id, @RequestBody UserDto body, Authentication auth) {
        boolean canManage = auth.getAuthorities().contains(new RoleAuthority("manage_users"));

        if (auth.getPrincipal() instanceof UserWithId user) {
            if (!canManage && !id.equals(user.getId())) {
                throw new InsufficientPermissionsException("Cannot modify other users unless 'manage_users' permission is present");
            }

            if (!canManage && body.userType != null) {
                throw new InsufficientPermissionsException("Changing 'user_type' requires manage_users permission");
            }

            return service.updateUser(id, body);
        }

        throw new InsufficientPermissionsException("Insufficient permissions to perform this operation");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Only current logged in user can delete his account. Administrator can delete any account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID of user to delete") @PathVariable("id") String id, Authentication auth) {
        boolean canManage = auth.getAuthorities().contains(new RoleAuthority("manage_users"));

        if (auth.getPrincipal() instanceof UserWithId user) {
            if (!canManage && !id.equals(user.getId())) {
                throw new InsufficientPermissionsException("Cannot delete other users unless 'manage_users' permission is present");
            }

            service.removeUser(id);
            return ResponseEntity.noContent().build();
        }

        throw new InsufficientPermissionsException("Insufficient permissions to perform this operation");
    }
}
