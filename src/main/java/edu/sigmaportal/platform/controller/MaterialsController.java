package edu.sigmaportal.platform.controller;

import edu.sigmaportal.platform.dto.FileDto;
import edu.sigmaportal.platform.dto.MaterialDto;

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
@RequestMapping("/api/material/")
@Tag(name = "material", description = "the material API")
public class MaterialsController {

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Find material by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material found", content = @Content(schema = @Schema(implementation = MaterialDto.class))),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content)
    })
    public MaterialDto findMaterialById(@Parameter(description = "ID of the material", required = true) @PathVariable String id) {
        return null;
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
    public MaterialDto createMaterial(@RequestBody MaterialDto body) {
        return null;
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material updated", content = @Content(schema = @Schema(implementation = MaterialDto.class))),
            @ApiResponse(responseCode = "400", description = "Material object is invalid", content = @Content),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content),
    })
    public MaterialDto updateMaterial(@Parameter(description = "ID of the material", required = true) @PathVariable String id, @RequestBody MaterialDto body) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Material deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Material not found", content = @Content)
    })
    public void deleteMaterial(@Parameter(description = "ID of the material", required = true) @PathVariable String id) {
    }
}
