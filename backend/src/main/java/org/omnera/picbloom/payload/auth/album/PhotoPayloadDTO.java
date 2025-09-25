package org.omnera.picbloom.payload.auth.album;

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
public class PhotoPayloadDTO {
    
    @NotBlank
    @Schema(description= "Photo Name", example="Selfie", requiredMode= Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description= "Description of the Photo", example="Selfie")
    private String description;


}
