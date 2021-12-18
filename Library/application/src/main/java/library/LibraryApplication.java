package library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@SpringBootApplication
@EntityScan("entities")
@ComponentScan(basePackages= { "controllers", "repository.configuration", "service.configuration" })
public class LibraryApplication {

	private static final Logger logger = LoggerFactory.getLogger(LibraryApplication.class);
		
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
		logger.info("LibraryApplication Started........");
	}
}
