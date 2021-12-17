package library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="controllers")
public class LibraryApplication {

	private static final Logger logger = LoggerFactory.getLogger(LibraryApplication.class);
		
	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
		logger.info("LibraryApplication Started........");
	}
}
