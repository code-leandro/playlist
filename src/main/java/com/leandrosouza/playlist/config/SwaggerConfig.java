package com.leandrosouza.playlist.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Playlist API", version = "1.1.0", description = "API para sugerir playlists baseado na temperatura"))
public class SwaggerConfig {
}

