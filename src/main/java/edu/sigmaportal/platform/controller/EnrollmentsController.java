package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.EnrollmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/enrollment")
@Tag(name = "enrollments", description = "the enrollments API")
public class EnrollmentsController {

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find enrollment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment found", content = @Content(schema = @Schema(implementation = EnrollmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Enrollment not found", content = @Content)
    })
    public EnrollmentDto findById(@PathVariable("id") String id) {
        return null;
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a enrollment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment updated", content = @Content(schema = @Schema(implementation = EnrollmentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid enrollment object", content = @Content)
    })
    public EnrollmentDto update(@PathVariable("id") String id, @RequestBody EnrollmentDto enrollment) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an enrollment", description = "Deletes an enrollment, effectively leaving a course.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Enrollment deleted"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    public void delete(@PathVariable("id") String id) {
    }
}
