package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.FileDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/file")
@Tag(name = "file", description = "the file API")
public class FilesController {

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Find file by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    public FileDto findById(@Parameter(description = "ID of the file") @PathVariable String id) {
        return null;
    }

    @GetMapping("/{id}/download/")
    @Operation(summary = "Download a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File bytes sent"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public void download(@Parameter(description = "ID of the file") @PathVariable String id, HttpServletResponse response) {
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload a file using JSON object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File created", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content)
    })
    public FileDto create(@RequestBody FileDto file) {
        return null;
    }

    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload a file using multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File created", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content)
    })
    public FileDto upload(@RequestParam("file") MultipartFile file) {
        return null;
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File updated", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    public FileDto update(@Parameter(description = "ID of the file") @PathVariable String id, @RequestBody FileDto file) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public void delete(@Parameter(description = "ID of the file") @PathVariable String id) {
    }
}
