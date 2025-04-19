package it.andrea.start.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApi30Config {

    @Value("${app.swagger.baseurl}")
    private String baseUrl;

    @Value("${app.swagger.baseurl-https}")
    private String baseUrlHttps;

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(Arrays.asList(createServer(baseUrl), createServer(baseUrlHttps)))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title("Base Tenant API Documentation").version("v1"));
    }

    private Server createServer(String url) {
        return new Server().url(url);
    }

}
