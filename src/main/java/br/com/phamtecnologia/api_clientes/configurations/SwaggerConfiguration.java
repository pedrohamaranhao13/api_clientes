package br.com.phamtecnologia.api_clientes.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clientes API")
                        .description("API para gerenciamento de clientes desenvolvida pala PHAM Tecnologia")
                        .version("V1.0.0")
                        .contact( new Contact()
                            .name("Pham Tecnologia")
                            .email("pedro.maranhao@yahoo.com.br")));
    }
}
