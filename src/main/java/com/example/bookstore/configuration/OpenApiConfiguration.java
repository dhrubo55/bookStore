package com.example.bookstore.configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dipanjal
 * @since 0.0.1
 */

@Configuration
@RequiredArgsConstructor
public class OpenApiConfiguration {

    private final String SECURITY_TYPE = "BearerAuth";

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("Book Store")
                .pathsToExclude("/dummy/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_TYPE))
                .components(getSecurityComponents())
                .info(getApiInfo());
    }

    private Components getSecurityComponents() {
        return new Components()
                .addSecuritySchemes(SECURITY_TYPE, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));
    }

    private Info getApiInfo() {
        return new Info()
                .title("Book Store Api")
                .version("v1")
                .description("A RESTful API Service for Book Store")
                .contact(getContactInfo())
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }

    private Contact getContactInfo() {
        return new Contact()
                .name("Mohibul Hassan Chowdhury")
                .email("mohibulhassan100@gmail.com")
                .url("https://github.com/dhrubo55");
    }
}
