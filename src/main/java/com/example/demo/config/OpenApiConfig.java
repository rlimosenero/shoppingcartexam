package com.example.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(

        info = @io.swagger.v3.oas.annotations.info.Info(
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Demo",
                        email = "demo@demo.com",
                        url = "https://localhost:8080/"
                ),
                description = "OpenApi documentation for Demo Shopping Cart",
                title = "OpenApi specification - Demo",
                version = "1.1.1",
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "Licence name",
                        url = "https://localhost:8080/"
                ),
                termsOfService = "Terms of service: This is just a demo for my exam"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://dummy.dum"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("shopping-cart-public")
                .pathsToMatch("/v1/**","/api/**")
                .build();
    }
}
