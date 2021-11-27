package tests.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import entities.Book;
import exceptions.service.book.BookISBNCannotBeEmptyException;
import exceptions.service.book.BookNameCannotBeEmptyException;
import exceptions.service.book.BookNotFoundException;
import exceptions.service.book.InvalidBookCostException;
import services.BookService;

public class BookServiceTests {
	private BookService bookService;
	
	public BookServiceTests() {
		bookService = new BookService();
	}
	
	@Test
	public void given_valid_parameters_should_add_book() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		int expectedId = 1;
		
		Integer id = bookService.add(book);
		
		assertThat(id).isEqualTo(expectedId);
	}
	
	@Test
	public void given_invalid_name_when_add_should_throw_an_exception() {
		String name = "";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		BookNameCannotBeEmptyException expectedException = new BookNameCannotBeEmptyException(book.id);
		
		BookNameCannotBeEmptyException thrown = (BookNameCannotBeEmptyException) catchThrowable(() -> bookService.add(book));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
	}
	
	@Test
	public void given_invalid_isbn_when_add_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		BookISBNCannotBeEmptyException expectedException = new BookISBNCannotBeEmptyException(book.id);
		
		BookISBNCannotBeEmptyException thrown = (BookISBNCannotBeEmptyException) catchThrowable(() -> bookService.add(book));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
	}
	
	@Test
	public void given_cost_name_when_add_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = new BigDecimal(-1);
		Book book = Book.create(name, ISBN, cost);
		InvalidBookCostException expectedException = new InvalidBookCostException(book.id, cost);
		
		InvalidBookCostException thrown = (InvalidBookCostException) catchThrowable(() -> bookService.add(book));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
		assertThat(thrown.cost).isEqualTo(cost);
	}
	
	@Test
	public void given_valid_parameters_should_update_book() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		Integer id = bookService.add(book);
		BigDecimal expectedCost = new BigDecimal(120);
		
		Book bookAdded = bookService.getById(id);
		bookAdded.bookCost = expectedCost;
		bookService.update(bookAdded);
		Book BookUpdated = bookService.getById(id);
		
		assertThat(BookUpdated.bookCost).isEqualTo(expectedCost);		
	}
	
	@Test
	public void given_invalid_name_when_update_should_throw_an_exception() {
		String name = "Hobbit";
		String nameAfterChange = "";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		Integer id = bookService.add(book);
		BookNameCannotBeEmptyException expectedException = new BookNameCannotBeEmptyException(id);
		
		Book bookAdded = bookService.getById(id);
		bookAdded.bookName = nameAfterChange;
		BookNameCannotBeEmptyException thrown = (BookNameCannotBeEmptyException) catchThrowable(() -> bookService.update(bookAdded));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
	}
	
	@Test
	public void given_invalid_isbn_when_update_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "1234567";
		String ISBNAfterChange = "";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		Integer id = bookService.add(book);
		BookISBNCannotBeEmptyException expectedException = new BookISBNCannotBeEmptyException(id);
		
		Book bookAdded = bookService.getById(id);
		bookAdded.ISBN = ISBNAfterChange;
		BookISBNCannotBeEmptyException thrown = (BookISBNCannotBeEmptyException) catchThrowable(() -> bookService.update(bookAdded));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(id);
	}
	
	@Test
	public void given_cost_name_when_update_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = new BigDecimal(100);
		BigDecimal costAfterChange = new BigDecimal(-1);
		Book book = Book.create(name, ISBN, cost);
		Integer id = bookService.add(book);
		InvalidBookCostException expectedException = new InvalidBookCostException(id, costAfterChange);
		
		Book bookAdded = bookService.getById(id);
		bookAdded.bookCost = costAfterChange;
		InvalidBookCostException thrown = (InvalidBookCostException) catchThrowable(() -> bookService.update(bookAdded));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
		assertThat(thrown.cost).isEqualTo(costAfterChange);
	}
	
	@Test
	public void given_not_existed_customer_when_update_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = new BigDecimal(100);
		Book book = Book.create(name, ISBN, cost);
		book.id = 100;
		BookNotFoundException expectedException = new BookNotFoundException(book.id);
		
		BookNotFoundException thrown = (BookNotFoundException) catchThrowable(() -> bookService.update(book));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
	}
	
	@Test
	public void given_valid_parameters_should_delete_book() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		Integer id = bookService.add(book);
		int expectedSize = 0;
		
		bookService.delete(id);
		List<Book> books = bookService.getEntities();
		
		assertThat(books.size()).isEqualTo(expectedSize);
	}
	
	@Test
	public void given_invalid_id_when_delete_should_throw_an_exception() {
		Integer id = 100;
		BookNotFoundException expectedException = new BookNotFoundException(id);
		
		BookNotFoundException thrown = (BookNotFoundException) catchThrowable(() -> bookService.delete(id));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(id);
	}
	
	public void given_valid_parameters_should_return_that_book_is_borrowed() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		Integer id = bookService.add(book);
		Book bookAdded = bookService.getById(id);
		bookAdded.borrowed = true;
		bookService.update(bookAdded);
		boolean expectedBorrowed = true;
				
		boolean borrowed = bookService.borrowed(id);
		
		assertThat(borrowed).isEqualTo(expectedBorrowed);
	}
	
	@Test
	public void given_invalid_id_when_check_if_book_is_borrowed_should_throw_an_exception() {
		Integer id = 100;
		BookNotFoundException expectedException = new BookNotFoundException(id);
		
		BookNotFoundException thrown = (BookNotFoundException) catchThrowable(() -> bookService.borrowed(id));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(id);
	}
}
