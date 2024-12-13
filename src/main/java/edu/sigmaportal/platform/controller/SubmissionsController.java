package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.SubmissionDto;
import edu.sigmaportal.platform.exception.BadRequestException;
import edu.sigmaportal.platform.exception.InsufficientPermissionsException;
import edu.sigmaportal.platform.security.RoleAuthority;
import edu.sigmaportal.platform.service.AssignmentService;
import edu.sigmaportal.platform.service.CourseService;
import edu.sigmaportal.platform.service.EnrollmentsService;
import edu.sigmaportal.platform.service.SubmissionService;
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
@RequestMapping("/api/submission")
@Tag(name = "submission", description = "the submission API")
public class SubmissionsController {

    private final SubmissionService submissions;
    private final AssignmentService assignments;
    private final EnrollmentsService enrollments;
    private final CourseService courses;

    public SubmissionsController(SubmissionService submissions, AssignmentService assignments, EnrollmentsService enrollments, CourseService courses) {
        this.submissions = submissions;
        this.assignments = assignments;
        this.enrollments = enrollments;
        this.courses = courses;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find submission by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission found", content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "404", description = "Submission not found", content = @Content)
    })
    public SubmissionDto findById(@Parameter(description = "ID of the submission") @PathVariable("id") String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        boolean canManage = auth.getAuthorities().contains(new RoleAuthority("manage_course_submissions"));
        String courseId = submissions.owningCourseId(id);
        if ((canManage && courses.owns(userId, courseId)) || submissions.owns(id, userId)) {
            return submissions.find(id);
        }

        throw new InsufficientPermissionsException("Access denied");
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
    @Secured("create_submission")
    public SubmissionDto create(@Valid @RequestBody SubmissionDto submission, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = assignments.owningCourseId(submission.assignmentId);
        if (enrollments.isEnrolled(courseId, userId)) {
            return submissions.create(userId, submission);
        }

        throw new InsufficientPermissionsException("Not enrolled");
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a submission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission created", content = @Content(schema = @Schema(implementation = SubmissionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid submission object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Submission not found", content = @Content)
    })
    @Secured("edit_submission")
    public SubmissionDto update(@Parameter(description = "ID of the submission") @PathVariable("id") String id, @RequestBody SubmissionDto submission, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (submissions.owns(id, userId)) {
            return submissions.update(id, submission);
        }

        throw new InsufficientPermissionsException("Not a submission owner");
    }

    @PostMapping(value = "/{id}/submit")
    @Operation(summary = "Submit a submission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submission submitted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Could not submit", content = @Content),
            @ApiResponse(responseCode = "404", description = "Submission not found", content = @Content)
    })
    @Secured("edit_submission")
    public ResponseEntity<Void> submit(@Parameter(description = "ID of the submission") @PathVariable("id") String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (submissions.owns(id, userId)) {
            if (submissions.submit(id)) {
                return ResponseEntity.noContent().build();
            } else {
                throw new BadRequestException("Already submitted");
            }
        }

        throw new InsufficientPermissionsException("Not a submission owner");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a submission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Submission deleted"),
            @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @Secured({"delete_submission", "manage_course_submissions"})
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the submission") @PathVariable("id") String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        boolean canManage = auth.getAuthorities().contains(new RoleAuthority("manage_course_submissions"));
        String courseId = submissions.owningCourseId(id);
        if ((canManage && courses.owns(userId, courseId)) || submissions.owns(id, userId)) {
            submissions.delete(id);
            return ResponseEntity.noContent().build();
        }

        throw new InsufficientPermissionsException("Not a submission or a related course owner");
    }
}
