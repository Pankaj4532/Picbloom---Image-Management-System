package org.omnera.picbloom.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDTO {
    
    @Size(min = 6, max = 20)
    @Schema(description="Password", example="Password123", requiredMode=Schema.RequiredMode.REQUIRED, minLength=6,maxLength=20)
    private String password;
}
