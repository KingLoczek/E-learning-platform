package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.GradeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/grade")
@Tag(name = "grades", description = "the grades API")
public class GradesController {

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a grade by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade found", content = @Content(schema = @Schema(implementation = GradeDto.class))),
            @ApiResponse(responseCode = "404", description = "Grade not found", content = @Content)
    })
    public GradeDto findById(@Parameter(description = "ID of the grade") @PathVariable("id") String id) {
        return null;
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade created", content = @Content(schema = @Schema(implementation = GradeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid grade object", content = @Content)
    })
    public GradeDto create(@RequestBody GradeDto grade) {
        return null;
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade updated", content = @Content(schema = @Schema(implementation = GradeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid grade object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Grade not found", content = @Content)
    })
    public GradeDto update(@Parameter(description = "ID of the grade") @PathVariable("id") String id, @RequestBody GradeDto grade) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Grade deleted"),
            @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    public void delete(@Parameter(description = "ID of the grade") @PathVariable("id") String id) {
    }
}
