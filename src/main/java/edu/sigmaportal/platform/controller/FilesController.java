package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.FileDto;
import edu.sigmaportal.platform.exception.EntityNotFoundException;
import edu.sigmaportal.platform.exception.InsufficientPermissionsException;
import edu.sigmaportal.platform.service.CourseService;
import edu.sigmaportal.platform.service.EnrollmentsService;
import edu.sigmaportal.platform.service.FileService;
import edu.sigmaportal.platform.util.AuthUtils;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/file")
@Tag(name = "file", description = "the file API")
public class FilesController {

    private final FileService service;
    private final EnrollmentsService enrolls;
    private final CourseService courses;

    public FilesController(FileService service, EnrollmentsService enrolls, CourseService courses) {
        this.service = service;
        this.enrolls = enrolls;
        this.courses = courses;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(description = "Find file by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    public FileDto findById(@Parameter(description = "ID of the file") @PathVariable String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (service.owns(userId, id))
            return service.find(id);

        Optional<String> courseId = service.connectedCourseId(id);
        if (courseId.isEmpty())
            throw new EntityNotFoundException("File not found");

        if (enrolls.isEnrolled(courseId.get(), userId) || courses.owns(userId, courseId.get())) {
            return service.find(id);
        }

        throw new EntityNotFoundException("File not found");
    }

    @GetMapping("/{id}/download/")
    @Operation(summary = "Download a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File bytes sent"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public void download(@Parameter(description = "ID of the file") @PathVariable String id, HttpServletResponse response, Authentication auth) {
        Optional<String> courseId = service.connectedCourseId(id);
        if (courseId.isEmpty())
            throw new EntityNotFoundException("File not found");

        String userId = AuthUtils.getUserId(auth);
        if (enrolls.isEnrolled(courseId.get(), userId) || courses.owns(userId, courseId.get())) {
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

        throw new EntityNotFoundException("File not found");
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload a file using JSON object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File created", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content)
    })
    @Secured("create_file")
    public FileDto create(@Valid @RequestBody FileDto file, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        return service.upload(userId, file);
    }

    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload a file using multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File created", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content)
    })
    @Secured("create_file")
    public FileDto upload(@RequestParam("file") MultipartFile file, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        return service.upload(userId, file);
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File updated", content = @Content(schema = @Schema(implementation = FileDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid file object", content = @Content),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content)
    })
    @Secured("edit_file")
    public FileDto update(@Parameter(description = "ID of the file") @PathVariable String id, @RequestBody FileDto file, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (service.owns(userId, id)) {
            return service.update(id, file);
        }

        throw new InsufficientPermissionsException("Not a file owner");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    @Secured("delete_file")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the file") @PathVariable String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (service.owns(userId, id)) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }

        throw new InsufficientPermissionsException("Not a file owner");
    }
}
