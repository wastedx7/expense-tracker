package com.micro.api_gateway.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.micro.api_gateway.service.JwtService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class JwtReactiveAuthManager implements ReactiveAuthenticationManager {
    
    private JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication){
        String token = authentication.getCredentials().toString();
        if(!jwtService.isTokenValid(token)){
            return Mono.error(new BadCredentialsException("Invalid JWT Token"));
        }
        String email = jwtService.extractEmail(token);
        return Mono.just(new UsernamePasswordAuthenticationToken(email, null, List.of()));
    }
    
}
