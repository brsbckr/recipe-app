package com.assignment.recipeapp.security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Springdoc.
 * This class is used to configure the Springdoc library.
 * Springdoc is used to generate the OpenAPI documentation.
 * It is annotated with @Configuration to indicate that it is a Spring configuration class.
 */
@Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class SpringdocConfig {}
