package com.micro.profile_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Authentication request/response payload")
public class AuthDTO {

    @Schema(description = "User email address", example = "user@example.com")
    private String email;

    @Schema(description = "User password", example = "P@ssw0rd")
    private String password;

    @Schema(description = "JWT token (populated in login response)")
    private String token;    
}
