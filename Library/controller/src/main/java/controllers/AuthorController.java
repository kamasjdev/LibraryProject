package controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import controllers.mapper.MapToDto;
import controllers.pojos.author.AddAuthor;
import controllers.pojos.author.UpdateAuthor;
import dto.AuthorDto;
import interfaces.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {

	private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
	private final AuthorService authorService;
	
	@Autowired
	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}
	
	@GetMapping("{authorId}")
	public AuthorDto getAuthor(@PathVariable Integer authorId) {
		logger.info(String.format("Getting author by id: %1$s", authorId));
		AuthorDto authorDto = authorService.getById(authorId);
		return authorDto;
	}
	
	@GetMapping
	public List<AuthorDto> getAll() {
		logger.info("Getting authors");
		List<AuthorDto> authors = authorService.getAll();
		return authors;
	}
	
	@PostMapping
	public int addAuthor(@RequestBody AddAuthor addAuthor) {
		logger.info("Adding new author");
		AuthorDto authorDto = MapToDto.mapAddAuthorToAuthorDto(addAuthor);
		logger.info("authorDto:");
		logger.info("authorFirstName " + authorDto.firstName);
		logger.info("authorLastName " + authorDto.lastName);
		logger.info("authorId " + authorDto.id);
		Integer id = authorService.add(authorDto);
		return id;
	}
	
	@PutMapping
	public void updateAuthor(@RequestBody UpdateAuthor updateAuthor) {
		logger.info("Updating author");
		AuthorDto authorDto = MapToDto.mapUpdateAuthorToAuthorDto(updateAuthor);
		authorService.update(authorDto);
	}
	
	@DeleteMapping("{authorId}")
	public void deleteAuthor(@PathVariable int authorId) {
		logger.info(String.format("Deleting author with id: %1$s", authorId));
		authorService.delete(authorId);
	}
	
	@GetMapping("{authorId}/details")
	public AuthorDto getAuthorDetails(@PathVariable int authorId) {
		logger.info(String.format("Getting author %1$s details", authorId));
		AuthorDto authorDto = authorService.getDetails(authorId);
		return authorDto;
	}
	
}
