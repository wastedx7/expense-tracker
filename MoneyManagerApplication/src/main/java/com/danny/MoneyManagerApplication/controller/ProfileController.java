package com.danny.MoneyManagerApplication.controller;

import com.danny.MoneyManagerApplication.DTO.AuthDTO;
import com.danny.MoneyManagerApplication.DTO.ProfileDTO;
import com.danny.MoneyManagerApplication.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    // ✅ Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registerProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
    }

    // ✅ Account activation endpoint
    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String activationToken) {
        boolean isActivated = profileService.activateProfile(activationToken);
        if (isActivated) {
            return ResponseEntity.ok("Profile activated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Activation token not found or already used");
        }
    }

    // ✅ Login endpoint (with error handling)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO) {
        try {
            // Check if account is active
            if (!profileService.isAccountActive(authDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message", "Account is not active. Please activate your account first."
                ));
            }

            // Authenticate user and generate token
            Map<String, Object> response = profileService.authenticationAndGenerateToken(authDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", e.getMessage()
            ));
        }
    }

    // ✅ Example endpoint to get current user profile
    @GetMapping("/me")
    public ResponseEntity<ProfileDTO> getCurrentProfile() {
        ProfileDTO profile = profileService.toDto(profileService.getCurrentProfile());
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/test")
    public String tset() {
        return "Test succesfull";
    }
    @GetMapping("/public")
    public ResponseEntity<ProfileDTO> getPublicProfile(){
        ProfileDTO profileDTO = profileService.getPublicProfile(null);
        return ResponseEntity.ok(profileDTO);
    }
    
}
