package controller.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class ControllerConfiguration {

	@Bean
	  public OpenAPI springShopOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("Library API")
	              .description("Spring library application")
	              .version("v0.0.1")
	              .license(new License().name("License")))
	              .externalDocs(new ExternalDocumentation()
	              .description("Library Wiki Documentation"));
	  }
}
