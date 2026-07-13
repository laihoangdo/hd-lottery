package com.hdplatform.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hdPlatformOpenApi() {

        return new OpenAPI()

                .info(new Info()

                        .title("HD Platform API")

                        .description("""
                                HD Platform REST API

                                Multi-Tenant Lottery Platform
                                Built with DDD + Hexagonal Architecture.
                                """)

                        .version("v1.0.0")

                        .contact(new Contact()

                                .name("HD Platform")

                                .email("support@hdplatform.com"))

                        .license(new License()

                                .name("MIT")))

                .externalDocs(

                        new ExternalDocumentation()

                                .description("Architecture Documentation")

                                .url("https://github.com/laihoangdo/hd-lottery"));
    }

}