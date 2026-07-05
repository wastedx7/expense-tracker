package com.micro.profile_service.DTO;


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
public class ProfileDTO {

    private long id;
    private UUID profileId;
    private String fullName;
    private String email;
    private String password;
    private String profileImageUrl;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
