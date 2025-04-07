package com.faculdade.usuario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuarioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuários")
                        .description("API para gerenciamento de usuários do sistema de reserva de salas")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Faculdade")
                                .email("contato@faculdade.com")
                                .url("https://www.faculdade.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
} 