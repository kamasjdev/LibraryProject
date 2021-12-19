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
import controllers.pojos.book.AddBook;
import controllers.pojos.book.BorrowBook;
import controllers.pojos.book.ReturnBook;
import controllers.pojos.book.UpdateBook;
import dto.AuthorDto;
import dto.BookAuthorDto;
import dto.BookDto;
import interfaces.AuthorService;
import interfaces.BookAuthorService;
import interfaces.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	private final BookService bookService;
	private final AuthorService authorService;
	private final BookAuthorService bookAuthorService;
	
	@Autowired
	public BookController(BookService bookService, AuthorService authorService, BookAuthorService bookAuthorService) {
		this.bookService = bookService;
		this.authorService = authorService;
		this.bookAuthorService = bookAuthorService;
	}
	
	@GetMapping("{bookId}")
	public BookDto getBook(@PathVariable Integer bookId) {
		logger.info(String.format("Getting book by id: %1$s", bookId));
		BookDto bookDto = bookService.getById(bookId);
		return bookDto;
	}
	
	@GetMapping
	public List<BookDto> getAll() {
		logger.info("Getting books");
		List<BookDto> books = bookService.getAll();
		return books;
	}
	
	@PostMapping
	public int addBook(@RequestBody AddBook addBook) {
		logger.info("Adding new book");
		BookDto bookDto = MapToDto.mapAddBookToBookDto(addBook);
		Integer id = bookService.add(bookDto);
		addBook.authorsIds.forEach(ba -> {
			AuthorDto authorDto = authorService.getById(ba);
			BookAuthorDto bookAuthorDto = new BookAuthorDto();
			bookAuthorDto.authorId = authorDto.id;
			bookAuthorDto.bookId = id;
			bookAuthorService.add(bookAuthorDto);
			BookAuthorDto bookAuthorDtoAdded = bookAuthorService.getById(id);
			bookDto.authors.add(bookAuthorDtoAdded);
		});
		
		return id;
	}
	
	@PutMapping
	public void updateBook(@RequestBody UpdateBook updateBook) {
		logger.info("Updating book");
		bookService.getById(updateBook.id);
		BookDto bookDto = MapToDto.mapUpdateBookToBookDto(updateBook);
		bookService.update(bookDto);
	}
	
	@DeleteMapping("{bookId}")
	public void deleteBook(@PathVariable int bookId) {
		logger.info(String.format("Deleting book with id: %1$s", bookId));
		bookService.deleteBookWithBookAuthors(bookId);
	}
	
	@GetMapping("{bookId}/details")
	public BookDto getBookDetails(@PathVariable int authorId) {
		logger.info(String.format("Getting book %1$s details", authorId));
		BookDto bookDto = bookService.getDetails(authorId);
		return bookDto;
	}
	
	@PostMapping("borrow")
	public void borrowBook(@RequestBody BorrowBook borrowBook) {
		logger.info(String.format("Customer with id: %1$s borrow book with id: %2$s", borrowBook.customerId, borrowBook.id));
		bookService.borrowBook(borrowBook.id, borrowBook.customerId);
	}
	
	@PostMapping("return")
	public void returnBook(@RequestBody ReturnBook returnBook) {
		logger.info(String.format("Customer with id: %1$s return book with id: %2$s", returnBook.customerId, returnBook.id));
		bookService.returnBook(returnBook.id, returnBook.customerId);
	}
}
