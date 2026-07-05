package com.micro.profile_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            // 1. Disable CSRF (microservices don't use cookies/browser-sessions)
            .csrf(csrf -> csrf.disable())
            // 2. Allow all internal traffic to flow through freely
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // 3. Completely turn off the default HTML form login page
            .formLogin(form -> form.disable())
            // 4. Completely turn off the HTTP Basic popup dialog (stops the 401 in Postman)
            .httpBasic(basic -> basic.disable())
            .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
