package com.micro.profile_service.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
public class ProfileController {

    private final ProfileService profileService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserDetailService appUserDetailsService;

    // ✅ Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registerProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
    }

    // ✅ Login endpoint (with error handling)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        try{
            // if credentials match, use authenticate to authenticate then do futher stuff
            authenticate(authDTO.getEmail(), authDTO.getPassword()); 
            // go to authUserDetailsService and load user by email you get from authDTO, store in userDetails somehow
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
        catch(BadCredentialsException ex) { // if email, password wrong, use this
            Map<String, Object> error = new HashMap<>();                        
            error.put("error", true);
            error.put("message", "email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }   
        catch(Exception ex) { // idk bout ts imma be fr
            Map<String, Object> error = new HashMap<>();                        
            error.put("error", true);
            error.put("message", "authorization failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    private void authenticate(String email, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    // ✅ Example endpoint to get current user profile
    @GetMapping("/me")
    public ResponseEntity<ProfileDTO> getCurrentProfile() {
        ProfileDTO profile = profileService.toDto(profileService.getCurrentProfile());
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/test")
    public String test() {
        return "Test successful";
    }
    
}
