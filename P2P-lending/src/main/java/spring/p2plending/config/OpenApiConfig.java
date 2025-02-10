    package spring.p2plending.config;

    import io.swagger.v3.oas.models.*;
    import io.swagger.v3.oas.models.info.*;
    import org.springdoc.core.models.GroupedOpenApi;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI()
                    .info(new Info()
                            .title("Stream API")
                            .version("2.0")
                            .description("API documentation for the P2P Lending Platform"));
        }

        @Bean
        public GroupedOpenApi publicApi() {
            return GroupedOpenApi.builder()
                    .group("Stream API")
                    .pathsToMatch("/api/**")
                    .build();
        }
    }
