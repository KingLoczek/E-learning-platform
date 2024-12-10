package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.CourseDto;
import edu.sigmaportal.platform.dto.EnrollDto;
import edu.sigmaportal.platform.exception.InsufficientPermissionsException;
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

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/course")
@Tag(name = "course", description = "the course API")
public class CourseController {

    private final CourseService service;
    private final EnrollmentsService enrolls;
    private final TopicService topics;

    public CourseController(CourseService service, EnrollmentsService enrolls, TopicService topics) {
        this.service = service;
        this.enrolls = enrolls;
        this.topics = topics;
    }

    @GetMapping(value = "/", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "List all courses")
    public Collection<CourseDto> allCourses() {
        return service.courses();
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a course by ID")
    public CourseDto findCourseById(@Parameter(description = "ID of the course to find") @PathVariable("id") String id) {
        return service.find(id);
    }

    @GetMapping(value = "/{id}/topics", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find topics associated with a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topics found", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    public Collection<String> findTopics(@Parameter(description = "ID of the associated course") @PathVariable("id") String id) {
        return topics.findOwnedBy(id);
    }

    @GetMapping(value = "/{id}/events", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find events associated with a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events found", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "404", description = "Course not found", content = @Content)
    })
    public Collection<String> findEvents(@Parameter(description = "ID of the associated course") @PathVariable("id") String id) {
        return null;
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course created", content = @Content(schema = @Schema(implementation = CourseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid course object", content = @Content)
    })
    @Secured("create_course")
    public CourseDto createCourse(@Valid @RequestBody CourseDto course, Authentication auth) {
        return service.create(AuthUtils.getUserId(auth), course);
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Edit a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated", content = @Content(schema = @Schema(implementation = CourseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid course object", content = @Content)
    })
    @Secured("edit_course")
    public CourseDto editCourse(@Parameter(description = "ID of the course to edit") @PathVariable("id") String id, @Valid @RequestBody CourseDto course, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (service.owns(userId, id)) {
            return service.update(id, course);
        }

        throw new InsufficientPermissionsException("Cannot edit a non-owned course");
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a course")
    @ApiResponses({
            @ApiResponse(description = "Course deleted", responseCode = "204"),
            @ApiResponse(description = "Course not found", responseCode = "404")
    })
    @Secured("delete_course")
    public ResponseEntity<Void> deleteCourse(@Parameter(description = "ID of the course to delete") @PathVariable("id") String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (service.owns(userId, id)) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }

        throw new InsufficientPermissionsException("Cannot delete a non-owned course");
    }

    @PostMapping(value = "/{id}/enroll", consumes = { APPLICATION_JSON_VALUE, APPLICATION_FORM_URLENCODED_VALUE })
    @Operation(summary = "Enroll into a course",
            description = "Enrolls logged in user into a course. If required correct `access_key` must be provided. Cannot be done as instructor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Enrolled into a course"),
            @ApiResponse(responseCode = "400", description = "Invalid `access_code`"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @Secured("enroll")
    public ResponseEntity<Void> enroll(@Parameter(description = "ID of the course to enroll into") @PathVariable("id") String id,
                                 @RequestBody EnrollDto enrollment, Authentication auth) {
        service.enroll(id, AuthUtils.getUserId(auth), enrollment.accessKey);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/leave")
    @Operation(summary = "Leave a course",
            description = "Removes the logged in user from the course. Cannot be done as a instructor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Left course"),
            @ApiResponse(responseCode = "400", description = "Cannot leave course")
    })
    public void leave(@Parameter(description = "ID of the course to leave") @PathVariable("id") String id, Authentication auth) {
        enrolls.remove(id, AuthUtils.getUserId(auth));
    }
}
