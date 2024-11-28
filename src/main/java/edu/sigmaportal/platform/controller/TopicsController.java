package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.TopicDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/topic")
@Tag(name = "topic", description = "the topic API")
public class TopicsController {

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find topic by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topic found", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content)
    })
    public TopicDto findTopicById(@Parameter(description = "ID of the topic to find") @PathVariable("id") String id) {
        return null;
    }

    @GetMapping(value = "/{id}/assignments", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find assignments associated with a topic", description = "Returns a list of associated assignments' IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments found", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content)
    })
    public Collection<String> findAssignments(@Parameter(description = "ID of the associated topic") @PathVariable("id") String id) {
        return null;
    }

    @GetMapping(value = "/{id}/materials", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find materials associated with a topic", description = "Returns a list of associated materials' IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materials found", content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))),
            @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content)
    })
    public Collection<String> findMaterials(@Parameter(description = "ID of the associated topic") @PathVariable("id") String id) {
        return null;
    }

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topic created", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid topic object", content = @Content)
    })
    public TopicDto createTopic(@RequestBody TopicDto topic) {
        return null;
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Change topic details",
            description = "Allows changing topic title, description, materials and assignments. Owning course id cannot be changed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Topic updated", content = @Content(schema = @Schema(implementation = TopicDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid topic object", content = @Content),
            @ApiResponse(responseCode = "404", description = "Topic not found", content = @Content)
    })
    public TopicDto updateTopic(@Parameter(description = "ID of the topic to update") @PathVariable("id") String id, @RequestBody TopicDto topic) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Topic deleted"),
            @ApiResponse(responseCode = "404", description = "Topic not found")
    })
    public void deleteTopic(@Parameter(description = "ID of the topic to delete") @PathVariable("id") String id) {
    }
}
