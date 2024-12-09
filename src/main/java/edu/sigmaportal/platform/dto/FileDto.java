package edu.sigmaportal.platform.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "File")
public class FileDto {
    @Schema(example = "8764", accessMode = Schema.AccessMode.READ_ONLY)
    public String id;

    @Schema(example = "the_c_programming_language.pdf")
    public String filename;

    @Schema(example = "PDF")
    public String type;

    @Schema(example = "aGVsbG8gd29ybGQ=", nullable = true)
    public String content;

    public FileDto(String id, String filename, String type, String content) {
        this.id = id;
        this.filename = filename;
        this.type = type;
        this.content = content;
    }
}
