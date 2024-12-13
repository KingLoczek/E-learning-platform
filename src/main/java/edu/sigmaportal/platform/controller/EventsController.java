package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.EventDto;
import edu.sigmaportal.platform.exception.InsufficientPermissionsException;
import edu.sigmaportal.platform.service.CourseService;
import edu.sigmaportal.platform.service.EnrollmentsService;
import edu.sigmaportal.platform.service.EventService;
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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/event")
@Tag(name = "event", description = "the event API")
public class EventsController {

    private final EventService events;
    private final CourseService courses;
    private final EnrollmentsService enrollments;

    public EventsController(EventService events, CourseService courses, EnrollmentsService enrollments) {
        this.events = events;
        this.courses = courses;
        this.enrollments = enrollments;
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find event by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    public EventDto findEventById(@Parameter(description = "ID of the event to find", required = true) @PathVariable String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = events.owningCourseId(id);
        if (enrollments.isEnrolled(courseId, userId)) {
            return events.find(id);
        }

        throw new InsufficientPermissionsException("Access denied");
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event created", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid event object", content = @Content)
    })
    @Secured("create_event")
    public EventDto createEvent(@RequestBody EventDto event, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (courses.owns(userId, event.courseId)) {
            return events.create(event);
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Change event details",
            description = "Allows changing event title, description, start_timestamp and end_timestamp. Owning course id cannot be changed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid event object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @Secured("edit_event")
    public EventDto updateEvent(@Parameter(description = "ID of the event to update", required = true) @PathVariable String id, @RequestBody EventDto event, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        if (courses.owns(userId, event.courseId)) {
            return events.update(id, event);
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @Secured("delete_event")
    public ResponseEntity<Void> deleteEvent(@Parameter(description = "ID of the event to delete", required = true) @PathVariable String id, Authentication auth) {
        String userId = AuthUtils.getUserId(auth);
        String courseId = events.owningCourseId(id);
        if (courses.owns(userId, courseId)) {
            events.delete(id);
            return ResponseEntity.noContent().build();
        }

        throw new InsufficientPermissionsException("Not a course owner");
    }
}
