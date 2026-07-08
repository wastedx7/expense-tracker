package com.micro.profile_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User profile data transfer object")
public class ProfileDTO {

    @Schema(description = "Internal database ID", example = "1")
    private long id;

    @Schema(description = "Public profile UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID profileId;

    @Schema(description = "User's full name", example = "John Doe")
    private String fullName;

    @Schema(description = "User email address", example = "user@example.com")
    private String email;

    @Schema(description = "User password (write-only)", example = "P@ssw0rd")
    private String password;

    @Schema(description = "URL to the user's profile image")
    private String profileImageUrl;

    @Schema(description = "Profile creation timestamp")
    private LocalDateTime createAt;

    @Schema(description = "Profile last update timestamp")
    private LocalDateTime updateAt;

}
