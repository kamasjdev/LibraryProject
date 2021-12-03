package tests.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import helpers.manager.customer.UpdateCustomer;
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
	private UpdateCustomer updateCustomer;
	
	public BookManagerTests() {
		authorService = Mockito.mock(AuthorService.class);
		bookService = Mockito.mock(BookService.class);
		bookAuthorService = Mockito.mock(BookAuthorService.class);
		updateCustomer = Mockito.mock(UpdateCustomer.class);
		bookManager = new BookManager(bookService, bookAuthorService, authorService, customerService, bookCustomerService, billService, updateCustomer);
	}
	
	@Test
	public void given_valid_parameters_should_edit_book() {
		Integer bookId = 1;
		String bookName = "ABC";
		String ISBN = "12343543245";
		BigDecimal bookCost = new BigDecimal(123.12);
		List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
		bookAuthors.add(BookAuthor.create(1, 1));
		bookAuthors.add(BookAuthor.create(1, 2));
		bookAuthors.add(BookAuthor.create(1, 3));
		bookAuthors.add(BookAuthor.create(1, 4));
		List<Integer> idsToChange = new ArrayList<Integer>();
		idsToChange.add(1);idsToChange.add(4);idsToChange.add(7);idsToChange.add(10);
		Book book = Book.create(bookName, ISBN, bookCost, bookAuthors);
		when(bookService.getById(bookId)).thenReturn(book);
		List<Author> authors = createSampleAuthors();	
		when(authorService.getEntities()).thenReturn(authors);
		when(authorService.getById(1)).thenReturn(authors.stream().filter(a -> a.id.equals(1)).findFirst().orElse(null));
		when(authorService.getById(2)).thenReturn(authors.stream().filter(a -> a.id.equals(2)).findFirst().orElse(null));
		when(authorService.getById(3)).thenReturn(authors.stream().filter(a -> a.id.equals(3)).findFirst().orElse(null));
		when(authorService.getById(4)).thenReturn(authors.stream().filter(a -> a.id.equals(4)).findFirst().orElse(null));
		when(authorService.getById(7)).thenReturn(authors.stream().filter(a -> a.id.equals(7)).findFirst().orElse(null));
		when(authorService.getById(10)).thenReturn(authors.stream().filter(a -> a.id.equals(10)).findFirst().orElse(null));
		int expectedUseBookServiceGetEntity = 1;
		int expectedUseAuthorServiceGetEntities = 1;
		int expectedUseBookAuthorServiceDelete = 2;
		int expectedUseBookAuthorServiceAdd = 2;
		int expectedUseAuthorServiceGetEntity = 8;
		int expectedUseBookServiceUpdate = 1;
		
		bookManager.editBook(bookId, bookName, ISBN, bookCost, idsToChange);
		
		verify(bookService, times(expectedUseBookServiceGetEntity)).getById(any());
		verify(authorService, times(expectedUseAuthorServiceGetEntities)).getEntities();
		verify(bookAuthorService, times(expectedUseBookAuthorServiceDelete)).delete(any());
		verify(bookAuthorService, times(expectedUseBookAuthorServiceAdd)).add(any(BookAuthor.class));
		verify(authorService, times(expectedUseAuthorServiceGetEntity)).getById(any(Integer.class));
		verify(bookService, times(expectedUseBookServiceUpdate)).update(any(Book.class));
	}
	
	private static List<Author> createSampleAuthors() {
		List<Author> authors = new ArrayList<Author>();
		
		Author author = Author.create("William", "Shakespeare");
		author.id = 1;
		authors.add(author);
		
		Author author2 = Author.create("Stanis³aw", "Lem");
		author2.id = 2;
		authors.add(author2);
		
		Author author3 = Author.create("Ernest", "Hemingway");
		author3.id = 3;
		authors.add(author3);
		
		Author author4 = Author.create("Józef", "Wybicki");
		author4.id = 4;
		authors.add(author4);
		
		Author author5 = Author.create("Abraham", "Lincoln");
		author5.id = 5;
		authors.add(author5);
		
		Author author6 = Author.create("Franz", "Kafka");
		author6.id = 6;
		authors.add(author6);
		
		Author author7 = Author.create("Julius", "Caesar");
		author7.id = 7;
		authors.add(author7);
		
		Author author8 = Author.create("Lewis", "Carroll");
		author8.id = 8;
		authors.add(author8);
		
		Author author9 = Author.create("Michael", "Faraday");
		author9.id = 9;
		authors.add(author9);
		
		Author author10 = Author.create("John Ronald Reuel", "Tolkien");
		author10.id = 10;
		authors.add(author10);
		
		Author author11 = Author.create("Henryk", "Sienkiewicz");
		author11.id = 11;
		authors.add(author11);
		
		Author author12 = Author.create("Boles³aw", "Prus");
		author12.id = 12;
		authors.add(author12);
		
		return authors;
	}
}
