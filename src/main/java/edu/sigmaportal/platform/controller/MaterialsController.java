package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.FileDto;
import edu.sigmaportal.platform.dto.MaterialDto;

import edu.sigmaportal.platform.exception.InsufficientPermissionsException;
import edu.sigmaportal.platform.service.MaterialService;
import edu.sigmaportal.platform.service.CourseService;
import edu.sigmaportal.platform.service.TopicService;
import edu.sigmaportal.platform.util.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/material/")
@Tag(name = "material", description = "the material API")
public class MaterialsController {

    private final TopicService topics;
    private final CourseService courses;
    private final MaterialService materials;

    public MaterialsController(TopicService topics, CourseService courses, MaterialService materials) {
        this.topics = topics;
        this.courses = courses;
        this.materials = materials;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find material by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material found", content = @Content(schema = @Schema(implementation = MaterialDto.class))),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content)
    })
    public MaterialDto findMaterialById(@Parameter(description = "ID of the material", required = true) @PathVariable String id) {
        return materials.find(id);
    }

    @GetMapping(value = "/{id}/files", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find files associated with a material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FileDto.class)))),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content)
    })
    public Collection<FileDto> findFiles(@Parameter(description = "ID of the material") @PathVariable String id) {
        return null;
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material created", content = @Content(schema = @Schema(implementation = MaterialDto.class))),
            @ApiResponse(responseCode = "400", description = "Material object is invalid", content = @Content),
    })
    @Secured("create_material")
    public MaterialDto createMaterial(@Valid @RequestBody MaterialDto body, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = topics.owningCourseId(body.topicId);
        if (courses.owns(userId, courseId)) {
            return materials.create(body, userId);
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material updated", content = @Content(schema = @Schema(implementation = MaterialDto.class))),
            @ApiResponse(responseCode = "400", description = "Material object is invalid", content = @Content),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content),
    })
    @Secured("edit_material")
    public MaterialDto updateMaterial(@Parameter(description = "ID of the material", required = true) @PathVariable String id, @Valid @RequestBody MaterialDto body, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = materials.owningCourseId(id);
        if (courses.owns(userId, courseId)) {
            return materials.update(id, body);
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Material deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content)
    })
    @Secured("delete_material")
    public ResponseEntity<Void> deleteMaterial(@Parameter(description = "ID of the material", required = true) @PathVariable String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = materials.owningCourseId(id);
        if (courses.owns(userId, courseId)) {
            materials.delete(id);
            return ResponseEntity.noContent().build();
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }
}
