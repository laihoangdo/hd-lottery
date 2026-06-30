package com.hdplatform.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hdPlatformOpenApi() {

        return new OpenAPI()

                .info(

                        new Info()

                                .title("HD Platform API")

                                .description("HD Platform REST API")

                                .version("v1")

                )

                .externalDocs(

                        new ExternalDocumentation()

                                .description("Blueprint")

                                .url("https://github.com/laihoangdo/hd-platform")

                );

    }

}