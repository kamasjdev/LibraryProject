package controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.BookAuthorDto;
import interfaces.BookAuthorService;

@RestController
@RequestMapping("/book-authors")
public class BookAuthorController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookAuthorController.class);
	private final BookAuthorService bookAuthorService;	
	
	@Autowired
	public BookAuthorController(BookAuthorService bookAuthorService) {
		this.bookAuthorService = bookAuthorService;
	}
	
	@GetMapping("{bookAuthorId}")
	public BookAuthorDto getBookAuthor(@PathVariable Integer bookAuthorId) {
		logger.info(String.format("Getting book author by id: %1$s", bookAuthorId));
		BookAuthorDto bookAuthorDto = bookAuthorService.getById(bookAuthorId);
		return bookAuthorDto;
	}
	
	@GetMapping
	public List<BookAuthorDto> getAll() {
		logger.info("Getting book authors");
		List<BookAuthorDto> bookAuthors = bookAuthorService.getAll();
		return bookAuthors;
	}
}
