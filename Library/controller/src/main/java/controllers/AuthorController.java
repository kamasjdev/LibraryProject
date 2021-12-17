package controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dto.AuthorDto;
import interfaces.AuthorService;

@RestController
public class AuthorController {

	private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
	
	private final AuthorService authorService;
	
	@Autowired
	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}
	
	@GetMapping("authors/{authorId}")
	public AuthorDto getAuthor(@PathVariable Integer authorId) {
		logger.info("Inside CustomerController........");
		return new AuthorDto();
	}
	
	@GetMapping("authors")
	public List<AuthorDto> getAll(@PathVariable Integer authorId) {
		logger.info("Inside CustomerController........");
		return new ArrayList<AuthorDto>();
	}
			
}
