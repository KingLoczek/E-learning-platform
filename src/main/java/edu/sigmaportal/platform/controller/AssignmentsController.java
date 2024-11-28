package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.AssignmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/assigment")
@Tag(name = "assigment", description = "the assigment API")
public class AssignmentsController {

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find assignment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment found", content = @Content(schema = @Schema(implementation = AssignmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public AssignmentDto findById(@Parameter(description = "ID of the assignment", required = true) @PathVariable String id) {
        return null;
    }

    @GetMapping(value = "/{id}/files", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find files associated with assignment", description = "Returns a list of associated files' IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files found", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public Collection<String> findFiles(@Parameter(description = "ID of the assignment") @PathVariable String id) {
        return null;
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment created", content = @Content(schema = @Schema(implementation = AssignmentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid assignment object", content = @Content)
    })
    public AssignmentDto create(@RequestBody AssignmentDto assigment) {
        return null;
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment updated", content = @Content(schema = @Schema(implementation = AssignmentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid assignment object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public AssignmentDto update(@Parameter(description = "ID of the assignment", required = true) @PathVariable String id, @RequestBody AssignmentDto assigment) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assignment deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public AssignmentDto deleteAssignment(@Parameter(description = "ID of the assignment", required = true) @PathVariable String id) {
        return null;
    }
}
