package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.SubmissionDto;
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
@RequestMapping("/api/submission")
@Tag(name = "submission", description = "the submission API")
public class SubmissionsController {

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find submission by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission found", content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found", content = @Content)
    })
    public SubmissionDto findById(@Parameter(description = "ID of the submission") @PathVariable("id") String id) {
        return null;
    }

    @GetMapping(value = "/{id}/grades", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find grades for an submission", description = "Returns a list of associated grades' IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grades found", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "404", description = "Submission not found", content = @Content)
    })
    public Collection<String> findGrades(@Parameter(description = "ID of the submission") @PathVariable("id") String id) {
        return null;
    }

    @GetMapping(value = "/{id}/files", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find files for an submission", description = "Returns a list of files' IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files found", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "404", description = "Submission not found", content = @Content)
    })
    public Collection<String> findFiles(@Parameter(description = "ID of the submission") @PathVariable("id") String id) {
        return null;
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new submission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission created", content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid submission object", content = @Content)
    })
    public SubmissionDto create(@RequestBody SubmissionDto submission) {
        return null;
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a submission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission created", content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid submission object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Submission not found", content = @Content)
    })
    public SubmissionDto update(@Parameter(description = "ID of the submission") @PathVariable("id") String id, @RequestBody SubmissionDto submission) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a submission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Submission deleted"),
            @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    public void delete(@Parameter(description = "ID of the submission") @PathVariable("id") String id) {
    }
}
