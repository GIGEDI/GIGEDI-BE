package gigedi.dev.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
        info =
                @Info(
                        title = "GIGEDI🚀",
                        description = "큐시즘 30기 밋업 프로젝트 기개디 API 문서",
                        version = "v1"),
        servers = {
            @Server(url = "http://localhost:8080", description = "서버 local URL"),
            @Server(url = "https://api.shoot-manage.com", description = "서버 배포 URL")
        })
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement =
                new SecurityRequirement()
                        .addList("bearerAuth")
                        .addList("figmaId")
                        .addList("fileId");

        Components components =
                new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization"))
                        .addSecuritySchemes(
                                "figmaId",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Figma-Id"))
                        .addSecuritySchemes(
                                "fileId",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("File-Id"));

        return new OpenAPI().components(components).addSecurityItem(securityRequirement);
    }
}
