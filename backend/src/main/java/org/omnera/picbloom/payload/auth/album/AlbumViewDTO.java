package org.omnera.picbloom.payload.auth.album;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AlbumViewDTO {

    private long id;

    @NotBlank
    @Schema(description="Album Name", example="Travel",requiredMode=Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description="Description of the Album", example="Description")
    private String description;
    
    private List<PhotoDTO> photos;
}
