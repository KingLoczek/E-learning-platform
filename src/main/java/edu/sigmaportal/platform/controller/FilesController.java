package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.FileDto;
import edu.sigmaportal.platform.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/file")
@Tag(name = "file", description = "the file API")
public class FilesController {

    private final FileService service;

    public FilesController(FileService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Find file by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    public FileDto findById(@Parameter(description = "ID of the file") @PathVariable String id) {
        return service.find(id);
    }

    @GetMapping("/{id}/download/")
    @Operation(summary = "Download a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File bytes sent"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public void download(@Parameter(description = "ID of the file") @PathVariable String id, HttpServletResponse response) {
        FileService.FileStream stream = service.stream(id);
        response.setContentType(stream.mimeType());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + stream.filename() + "\"");
        try (InputStream s = stream.handle()) {
            s.transferTo(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload a file using JSON object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File created", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content)
    })
    public FileDto create(@Valid @RequestBody FileDto file) {
        return service.upload(file);
    }

    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload a file using multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File created", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content)
    })
    public FileDto upload(@RequestParam("file") MultipartFile file) {
        return service.upload(file);
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File updated", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    public FileDto update(@Parameter(description = "ID of the file") @PathVariable String id, @RequestBody FileDto file) {
        return service.update(id, file);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the file") @PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
