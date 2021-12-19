package controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.BookCustomerDto;
import interfaces.BookCustomerService;

@RestController
@RequestMapping("/book-customers")
public class BookCustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookCustomerController.class);
	private final BookCustomerService bookCustomerService;	
	
	@Autowired
	public BookCustomerController(BookCustomerService bookAuthorService) {
		this.bookCustomerService = bookAuthorService;
	}
	
	@GetMapping("{bookCustomerId}")
	public BookCustomerDto getBookAuthor(@PathVariable Integer bookCustomerId) {
		logger.info(String.format("Getting book author by id: %1$s", bookCustomerId));
		BookCustomerDto bookCustomerDto = bookCustomerService.getById(bookCustomerId);
		return bookCustomerDto;
	}
	
	@GetMapping
	public List<BookCustomerDto> getAll() {
		logger.info("Getting book authors");
		List<BookCustomerDto> bookCustomers = bookCustomerService.getAll();
		return bookCustomers;
	}
}
