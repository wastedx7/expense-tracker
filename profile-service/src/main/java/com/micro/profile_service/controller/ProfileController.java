package com.micro.profile_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.micro.profile_service.DTO.AuthDTO;
import com.micro.profile_service.DTO.ProfileDTO;
import com.micro.profile_service.service.ProfileService;
import com.micro.profile_service.service.AppUserDetailService;
import com.micro.profile_service.service.JwtService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Tag(name = "Profile", description = "User profile management API")
public class ProfileController {

    private final ProfileService profileService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserDetailService appUserDetailsService;

    @Operation(summary = "Register a new user", description = "Creates a new user profile and returns the profile data")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Profile created successfully",
            content = @Content(schema = @Schema(implementation = ProfileDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registerProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
    }

    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token in both the response body and an httpOnly cookie")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(schema = @Schema(implementation = AuthDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid email or password"),
        @ApiResponse(responseCode = "401", description = "Authorization failed")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        try{
            authenticate(authDTO.getEmail(), authDTO.getPassword());
            final UserDetails userDetails = appUserDetailsService.loadUserByUsername(authDTO.getEmail());
            final String jwtToken = jwtService.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthDTO(authDTO.getEmail(), authDTO.getPassword(), jwtToken));
        } 
        catch(BadCredentialsException ex) {
            Map<String, Object> error = new HashMap<>();                        
            error.put("error", true);
            error.put("message", "email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }   
        catch(Exception ex) {
            Map<String, Object> error = new HashMap<>();                        
            error.put("error", true);
            error.put("message", "authorization failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    private void authenticate(String email, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @Operation(summary = "Get current user profile", description = "Returns the profile of the currently authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
            content = @Content(schema = @Schema(implementation = ProfileDTO.class))),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<ProfileDTO> getCurrentProfile() {
        ProfileDTO profile = profileService.toDto(profileService.getCurrentProfile());
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Health check", description = "Simple endpoint to verify the service is running")
    @GetMapping("/test")
    public String test() {
        return "Test successful";
    }
    
}
