package com.micro.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("profile-service", r -> r
                .path("/profile/**")
                // .filters(f -> f.stripPrefix(1))
                .uri("lb://PROFILE-SERVICE")
            )
            .build();
    }
}
