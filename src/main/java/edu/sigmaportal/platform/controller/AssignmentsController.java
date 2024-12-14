package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.AssignmentDto;
import edu.sigmaportal.platform.exception.InsufficientPermissionsException;
import edu.sigmaportal.platform.service.AssignmentService;
import edu.sigmaportal.platform.service.CourseService;
import edu.sigmaportal.platform.service.EnrollmentsService;
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
@RequestMapping("/api/assigment")
@Tag(name = "assigment", description = "the assigment API")
public class AssignmentsController {

    private final AssignmentService assignments;
    private final TopicService topics;
    private final CourseService courses;
    private final EnrollmentsService enrolls;

    public AssignmentsController(AssignmentService assignments, TopicService topics, CourseService courses, EnrollmentsService enrolls) {
        this.assignments = assignments;
        this.topics = topics;
        this.courses = courses;
        this.enrolls = enrolls;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find assignment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment found", content = @Content(schema = @Schema(implementation = AssignmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public AssignmentDto findById(@Parameter(description = "ID of the assignment", required = true) @PathVariable String id) {
        return assignments.find(id);
    }

    @GetMapping(value = "/{id}/files", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find files associated with assignment", description = "Returns a list of associated files' IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files found", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    public Collection<String> findFiles(@Parameter(description = "ID of the assignment") @PathVariable String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = assignments.owningCourseId(id);
        if (enrolls.isEnrolled(courseId, userId) || courses.owns(userId, courseId)) {
            return assignments.files(id);
        }
        return null;
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment created", content = @Content(schema = @Schema(implementation = AssignmentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid assignment object", content = @Content)
    })
    @Secured("create_assignment")
    public AssignmentDto create(@Valid @RequestBody AssignmentDto assigment, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = topics.owningCourseId(assigment.topicId);
        if (courses.owns(userId, courseId)) {
            return assignments.create(userId, assigment);
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment updated", content = @Content(schema = @Schema(implementation = AssignmentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid assignment object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    @Secured("edit_assignment")
    public AssignmentDto update(@Parameter(description = "ID of the assignment", required = true) @PathVariable String id, @RequestBody AssignmentDto assigment, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = assignments.owningCourseId(id);
        if (courses.owns(userId, courseId)) {
            return assignments.update(id, assigment);
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assignment deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Assignment not found", content = @Content)
    })
    @Secured("delete_assignment")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the assignment", required = true) @PathVariable String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = assignments.owningCourseId(id);
        if (courses.owns(userId, courseId)) {
            assignments.delete(id);
            return ResponseEntity.noContent().build();
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }
}
