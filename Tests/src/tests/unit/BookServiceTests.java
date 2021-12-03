package tests.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import entities.Book;
import exceptions.service.book.BookISBNCannotBeEmptyException;
import exceptions.service.book.BookNameCannotBeEmptyException;
import exceptions.service.book.BookNotFoundException;
import exceptions.service.book.InvalidBookCostException;
import interfaces.BookRepository;
import services.BookService;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTests {
	private BookService bookService;
	private BookRepository bookRepository;
	
	public BookServiceTests() {
		this.bookRepository = Mockito.mock(BookRepository.class);
		bookService = new BookService(bookRepository);
	}
	
	@Test
	public void given_valid_parameters_should_add_book() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		int expectedId = 1;
		when(bookRepository.add(book)).thenReturn(expectedId);
		
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
		BigDecimal expectedCost = new BigDecimal(120);
		Book book = Book.create(name, ISBN, expectedCost);
		Integer bookId = 1;
		book.id = bookId;
		when(bookRepository.get(bookId)).thenReturn(book);
		
		bookService.update(book);

		verify(bookRepository, times(1)).update(any(Book.class));
		Book BookUpdated = bookService.getById(bookId);
		assertThat(BookUpdated.bookCost).isEqualTo(expectedCost);
	}
	
	@Test
	public void given_invalid_name_when_update_should_throw_an_exception() {
		String nameAfterChange = "";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(nameAfterChange, ISBN, cost);
		Integer bookId = 1;
		book.id = bookId;
		BookNameCannotBeEmptyException expectedException = new BookNameCannotBeEmptyException(bookId);
		
		BookNameCannotBeEmptyException thrown = (BookNameCannotBeEmptyException) catchThrowable(() -> bookService.update(book));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
	}
	
	@Test
	public void given_invalid_isbn_when_update_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		Integer id = 1;
		book.id = id;
		BookISBNCannotBeEmptyException expectedException = new BookISBNCannotBeEmptyException(id);

		BookISBNCannotBeEmptyException thrown = (BookISBNCannotBeEmptyException) catchThrowable(() -> bookService.update(book));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(id);
	}
	
	@Test
	public void given_cost_name_when_update_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = new BigDecimal(-1);
		Book book = Book.create(name, ISBN, cost);
		Integer id = 1;
		book.id = id;
		InvalidBookCostException expectedException = new InvalidBookCostException(id, cost);
		
		InvalidBookCostException thrown = (InvalidBookCostException) catchThrowable(() -> bookService.update(book));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
		assertThat(thrown.cost).isEqualTo(cost);
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
		Integer id = 1;
		book.id = id;
		int expectedSize = 0;
		when(bookRepository.get(id)).thenReturn(book);
		
		bookService.delete(id);
		
		verify(bookRepository, times(1)).delete(any(Book.class));
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
	
	@Test
	public void given_valid_parameters_should_return_that_book_is_borrowed() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		Book book = Book.create(name, ISBN, cost);
		Integer id = 1;
		book.id = id;
		book.borrowed = true;
		when(bookRepository.get(id)).thenReturn(book);
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
