package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.GradeDto;
import edu.sigmaportal.platform.exception.InsufficientPermissionsException;
import edu.sigmaportal.platform.service.CourseService;
import edu.sigmaportal.platform.service.GradeService;
import edu.sigmaportal.platform.service.SubmissionService;
import edu.sigmaportal.platform.util.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/grade")
@Tag(name = "grades", description = "the grades API")
public class GradesController {

    private final GradeService grades;
    private final SubmissionService submissions;
    private final CourseService courses;

    public GradesController(GradeService grades, SubmissionService submissions, CourseService courses) {
        this.grades = grades;
        this.submissions = submissions;
        this.courses = courses;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a grade by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade found", content = @Content(schema = @Schema(implementation = GradeDto.class))),
            @ApiResponse(responseCode = "404", description = "Grade not found", content = @Content)
    })
    public GradeDto findById(@Parameter(description = "ID of the grade") @PathVariable("id") String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (grades.hasAccess(id, userId)) {
            return grades.find(id);
        }

        throw new InsufficientPermissionsException("Access denied");
    }

    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade created", content = @Content(schema = @Schema(implementation = GradeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid grade object", content = @Content)
    })
    @Secured("create_grade")
    public GradeDto create(@RequestBody GradeDto grade, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = submissions.owningCourseId(grade.submissionId);
        if (courses.owns(userId, courseId)) {
            return grades.create(userId, grade);
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grade updated", content = @Content(schema = @Schema(implementation = GradeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid grade object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Grade not found", content = @Content)
    })
    @Secured("edit_grade")
    public GradeDto update(@Parameter(description = "ID of the grade") @PathVariable("id") String id, @RequestBody GradeDto grade, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = submissions.owningCourseId(grade.submissionId);
        if (courses.owns(userId, courseId)) {
            return grades.update(id, grade);
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a grade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Grade deleted"),
            @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @Secured("delete_grade")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the grade") @PathVariable("id") String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = grades.owningCourseId(id);
        if (courses.owns(userId, courseId)) {
            grades.delete(id);
            return ResponseEntity.noContent().build();
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }
}
