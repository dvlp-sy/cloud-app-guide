package com.guide.cloud_app.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cloud Application Guide API")
                        .description("클라우드 애플리케이션 실습용 API 명세서")
                        .version("v1.0.0"));
    }

    @Bean
    public GroupedOpenApi cagApi() {
        return GroupedOpenApi.builder()
                .group("cag-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
