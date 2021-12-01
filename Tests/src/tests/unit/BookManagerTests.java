package tests.unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import managers.BookManager;
import services.AuthorService;
import services.BillService;
import services.BookAuthorService;
import services.BookCustomerService;
import services.BookService;
import services.CustomerService;

@RunWith(MockitoJUnitRunner.class)
public class BookManagerTests {
	private BookManager bookManager;
	private BookService bookService;
	private BookAuthorService bookAuthorService;
	private AuthorService authorService;
	private CustomerService customerService;
	private BookCustomerService bookCustomerService;
	private BillService billService;
	
	public BookManagerTests() {
		authorService = Mockito.mock(AuthorService.class);
		bookService = Mockito.mock(BookService.class);
		bookManager = new BookManager(bookService, bookAuthorService, authorService, customerService, bookCustomerService, billService);
	}
	
	@Test
	public void given_valid_parameters_should_edit_book() {
		
	}
}
