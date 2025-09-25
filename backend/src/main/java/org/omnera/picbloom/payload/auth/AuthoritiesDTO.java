package org.omnera.picbloom.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthoritiesDTO {

    @NotBlank
    @Schema(description="Authorities", example="USER",
    requiredMode=Schema.RequiredMode.REQUIRED)
    private String authorities;
}
