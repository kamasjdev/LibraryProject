package service.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.AuthorDto;
import dto.BookAuthorDto;
import dto.BookCustomerDto;
import dto.BookDto;
import entities.Author;
import entities.Book;
import entities.BookAuthor;
import exceptions.service.author.AuthorNotFoundException;
import exceptions.service.book.BookAuthorsCannotBeEmptyOrNullException;
import exceptions.service.book.BookISBNCannotBeEmptyException;
import exceptions.service.book.BookNameCannotBeEmptyException;
import exceptions.service.book.BookNotFoundException;
import exceptions.service.book.InvalidBookCostException;
import helpers.manager.customer.UpdateCustomer;
import helpers.services.mappings.Mapper;
import interfaces.AuthorService;
import interfaces.BillService;
import interfaces.BookAuthorService;
import interfaces.BookCustomerService;
import interfaces.BookService;
import interfaces.CustomerService;
import repository.BookRepository;
import services.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	private BookService bookService;
	private BookRepository bookRepository;
	private AuthorService authorService;
	private BookAuthorService bookAuthorService;
	private UpdateCustomer updateCustomer;
	private CustomerService customerService;
	private BookCustomerService bookCustomerService;
	private BillService billService;
	
	@Before 
	public void init() {
		bookRepository = Mockito.mock(BookRepository.class);
		authorService = Mockito.mock(AuthorService.class);
		bookAuthorService = Mockito.mock(BookAuthorService.class);
		customerService = Mockito.mock(CustomerService.class);
		bookCustomerService = Mockito.mock(BookCustomerService.class);
		billService = Mockito.mock(BillService.class);
		updateCustomer = new UpdateCustomer();
		bookService = new BookServiceImpl(bookRepository, authorService, bookAuthorService, updateCustomer, customerService, bookCustomerService, billService);
    }
	
	@Test
	public void given_valid_parameters_should_add_book() {
		BookDto book = createBook();
		doReturn(2).when(authorService).getCount();
		
		bookService.add(book);
		
		verify(bookRepository, times(1)).add(any(Book.class));
	}
	
	@Test
	public void given_invalid_name_when_add_should_throw_an_exception() {
		String name = "";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		BookDto book = createBook(name, ISBN, cost);
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
		BookDto book = createBook(name, ISBN, cost);
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
		BookDto book = createBook(name, ISBN, cost);
		InvalidBookCostException expectedException = new InvalidBookCostException(book.id, cost);
		
		InvalidBookCostException thrown = (InvalidBookCostException) catchThrowable(() -> bookService.add(book));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.bookId).isEqualTo(book.id);
		assertThat(thrown.cost).isEqualTo(cost);
	}
	
	@Test
	public void given_book_without_authors_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal expectedCost = new BigDecimal(120);
		Book book = Book.create(name, ISBN, expectedCost);
		Integer bookId = 1;
		book.id = bookId;
		BookDto bookDto = createBook(name, ISBN, expectedCost);
		bookDto.id = bookId;
		doReturn(book).when(bookRepository).get(any(Integer.class));
		doReturn(2).when(authorService).getCount();
		BookAuthorsCannotBeEmptyOrNullException expectedException = new BookAuthorsCannotBeEmptyOrNullException();
		
		BookAuthorsCannotBeEmptyOrNullException thrown = (BookAuthorsCannotBeEmptyOrNullException) catchThrowable(() -> bookService.update(bookDto));

		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
	}
	
	@Test
	public void given_book_with_invalid_authors_should_throw_an_exception() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal expectedCost = new BigDecimal(120);
		Book book = Book.create(name, ISBN, expectedCost);
		Integer bookId = 1;
		book.id = bookId;
		BookDto bookDto = createBook(name, ISBN, expectedCost);
		bookDto.id = bookId;
		bookDto.authors.add(new BookAuthorDto());
		doReturn(book).when(bookRepository).get(any(Integer.class));
		doReturn(2).when(authorService).getCount();
		AuthorNotFoundException expectedException = new AuthorNotFoundException(null);
		
		AuthorNotFoundException thrown = (AuthorNotFoundException) catchThrowable(() -> bookService.update(bookDto));

		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
	}
	
	@Test
	public void given_valid_parameters_should_update_book() {
		String name = "Hobbit";
		String ISBN = "123456";
		BigDecimal expectedCost = new BigDecimal(120);
		Book book = Book.create(name, ISBN, expectedCost);
		Integer bookId = 1;
		book.id = bookId;
		BookDto bookDto = createBook(name, ISBN, expectedCost);
		bookDto.id = bookId;
		BookAuthorDto bookAuthorDto2 = new BookAuthorDto();
		bookAuthorDto2.authorId = 3;
		BookAuthorDto bookAuthorDto3 = new BookAuthorDto();
		bookAuthorDto3.authorId = 4;
		bookDto.authors.add(bookAuthorDto2);
		bookDto.authors.add(bookAuthorDto3);
		Author author = Author.create("Auth1", "Auths");
		Author author2 = Author.create("Auth2", "Asuths");
		Author author3 = Author.create("AutAF2", "Afasths");
		Author author4 = Author.create("235AutAF2", "Afasth42s");
		author.id = 1;
		author2.id = 2;
		author3.id = 3;
		author4.id = 4;
		BookAuthor bookAuthor = createBookAuthor(book, author);
		BookAuthor bookAuthor2 = createBookAuthor(book, author2);
		bookAuthor.id = 1;
		bookAuthor2.id = 2;
		book.authors.add(bookAuthor);
		book.authors.add(bookAuthor2);
		bookDto.authors.add(Mapper.mapToBookAuthorDto(bookAuthor));
		doReturn(book).when(bookRepository).get(any(Integer.class));
		doReturn(2).when(authorService).getCount();
		doReturn(Mapper.mapToAuthorDto(author)).when(authorService).getById(author.id);
		doReturn(Mapper.mapToAuthorDto(author2)).when(authorService).getById(author2.id);
		doReturn(Mapper.mapToAuthorDto(author3)).when(authorService).getById(author3.id);
		doReturn(Mapper.mapToAuthorDto(author4)).when(authorService).getById(author4.id);
		
		bookService.update(bookDto);

		verify(bookAuthorService, times(1)).delete(any(Integer.class));
		verify(bookAuthorService, times(2)).add(any(BookAuthorDto.class));
	}
	
	@Test
	public void given_invalid_name_when_update_should_throw_an_exception() {
		String nameAfterChange = "";
		String ISBN = "123456";
		BigDecimal cost = BigDecimal.ONE;
		BookDto book = createBook(nameAfterChange, ISBN, cost);
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
		BookDto book = createBook(name, ISBN, cost);
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
		BookDto book = createBook(name, ISBN, cost);
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
		BookDto book = createBook(name, ISBN, cost);
		book.id = 100;
		BookAuthorDto bookAuthor = createBookAuthor();
		bookAuthor.bookId = book.id;
		bookAuthor.id = 1;
		book.authors.add(bookAuthor);
		doReturn(2).when(authorService).getCount();
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
		doReturn(book).when(bookRepository).getBookWithoutAuthors(any(Integer.class));
		
		bookService.delete(id);
		
		verify(bookRepository, times(1)).delete(any(Integer.class));
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
	
	private BookDto createBook() {
		BookDto bookDto = new BookDto();
		bookDto.bookName = "Lord of the Rings";
		bookDto.bookCost = new BigDecimal(100);
		bookDto.borrowed = false;
		bookDto.ISBN = "12354563";
		return bookDto;
	}
	
	private BookDto createBook(String bookName, String ISBN, BigDecimal cost) {
		BookDto bookDto = new BookDto();
		bookDto.bookName = bookName;
		bookDto.bookCost = cost;
		bookDto.borrowed = false;
		bookDto.ISBN = ISBN;
		bookDto.authors = new HashSet<BookAuthorDto>();
		bookDto.customers = new HashSet<BookCustomerDto>();
		return bookDto;
	}
	
	private AuthorDto createAuthor() {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = "Tommy";
		authorDto.lastName = "Vercetti";
		return authorDto;
	}
	
	private BookAuthorDto createBookAuthor() {
		BookAuthorDto bookAuthorDto = new BookAuthorDto();
		bookAuthorDto.authorId = 1;
		bookAuthorDto.author = createAuthor();
		bookAuthorDto.author.id = 1;
		return bookAuthorDto;
	}
	
	private BookAuthor createBookAuthor(Book book, Author author) {
		BookAuthor bookAuthor = BookAuthor.create(book.id, author.id);
		bookAuthor.book = book;
		bookAuthor.author = author;
		return bookAuthor;
	}
}
