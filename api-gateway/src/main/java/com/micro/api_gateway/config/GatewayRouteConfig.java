package com.micro.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GatewayRouteConfig {

    private final EmailHeaderGatewayFilter emailHeaderGatewayFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("profile-service", r -> r
                .path("/profile/**")
                .filters(f -> f.filter(emailHeaderGatewayFilter.apply(new EmailHeaderGatewayFilter.Config())))
                .uri("lb://profile-service")
            )
            .build();
    }
}
