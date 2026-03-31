package com.example.sportcontrol.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "SportControl API",
        version = "1.0",
        description = "API for matches, tournamnts and sports",
        contact = @Contact(name = "SportControl Team")
    )
)
public class OpenApiConfig {
}