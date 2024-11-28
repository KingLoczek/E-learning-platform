package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.EventDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/event")
@Tag(name = "event", description = "the event API")
public class EventsController {

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find event by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    public EventDto findEventById(@Parameter(description = "ID of the event to find", required = true) @PathVariable String id) {
        return null;
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event created", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid event object", content = @Content)
    })
    public EventDto createEvent(@RequestBody EventDto event) {
        return null;
    }

    @PatchMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Change event details",
            description = "Allows changing event title, description, start_timestamp and end_timestamp. Owning course id cannot be changed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid event object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    public EventDto updateEvent(@Parameter(description = "ID of the event to update", required = true) @PathVariable String id, @RequestBody EventDto event) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public void deleteEvent(@Parameter(description = "ID of the event to delete", required = true) @PathVariable String id) {
    }
}
