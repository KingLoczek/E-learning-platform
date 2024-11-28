package edu.sigmaportal.platform.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "SigmaPortal", description = "SigmaPortal Platform API"))
public class OpenAPIConfig {
}
