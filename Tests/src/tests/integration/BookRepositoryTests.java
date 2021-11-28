package tests.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import entities.Customer;
import interfaces.BookRepository;
import interfaces.MapEntity;
import mappings.AuthorMapping;
import mappings.BookAuthorMapping;
import mappings.BookCustomerMapping;
import mappings.BookMapping;
import mappings.CustomerMapping;
import repository.BookRepositoryImpl;

public class BookRepositoryTests extends BaseTest {
	private BookRepository bookRepository;
	private MapEntity<Book> mapper;
	private MapEntity<BookAuthor> bookAuthorMapper;
	private MapEntity<Author> authorMapper;
	private MapEntity<BookCustomer> bookCustomerMapper;
	private MapEntity<Customer> customerMapper;
	
	public BookRepositoryTests() {
		mapper = new BookMapping();
		bookAuthorMapper = new BookAuthorMapping();
		authorMapper = new AuthorMapping();
		bookCustomerMapper = new BookCustomerMapping();
		customerMapper = new CustomerMapping();
		bookRepository = new BookRepositoryImpl(dbClient, mapper, bookAuthorMapper, authorMapper, bookCustomerMapper, customerMapper);
	}
	
	@Test
	public void given_valid_parameters_should_return_list_book() {
		List<Book> books = bookRepository.getAll();
		
		assertThat(books).isNotNull();
		assertThat(books.size()).isGreaterThan(0);
	}
	
	@Test
	public void given_valid_id_should_return_book() {
		Integer id = 1;
		
		Book book = bookRepository.get(id);
		
		assertThat(book).isNotNull();
		assertThat(book.id).isEqualTo(id);
	}
	
	@Test
	public void given_invalid_id_shouldnt_return_book() {
		Integer id = -1;
		
		Book book = bookRepository.get(id);
		
		assertThat(book).isNull();
	}
	
	@Test
	public void given_valid_parameters_should_return_book_details() {
		Integer id = 1;
		
		Book book = bookRepository.getBookDetails(id);
		
		assertThat(book).isNotNull();
	}
	
	@Test
	public void given_invalid_parameters_shouldnt_return_book_details() {
		Integer id = -1;
		
		Book book = bookRepository.getBookDetails(id);
		
		assertThat(book).isNull();
	}
	
	@Test
	public void given_valid_parameters_should_add_book() {
		String bookName = "Abc";
		String ISBN = "1234567257";
		BigDecimal cost = new BigDecimal(123.12);
		Book book = Book.create(bookName, ISBN, cost);
		BookAuthor bookAuthor = BookAuthor.create(null, 8);
		BookAuthor bookAuthor2 = BookAuthor.create(null, 9);
		book.authors.add(bookAuthor);
		book.authors.add(bookAuthor2);
		
		Integer id = bookRepository.add(book);
		
		assertThat(id).isNotNull();
	}
	
	@Test
	public void given_valid_parameters_should_update_book() {
	 	String bookName = "AbcXas";
	 	String ISBN = "123456757";
	 	BigDecimal cost = new BigDecimal(1231.22);
	 	Integer bookId = 9;
	 	Integer authorId = 8;
	 	BookAuthor bookAuthor = BookAuthor.create(bookId, authorId);
	 	bookAuthor.id = 6;
	 	List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
	 	bookAuthors.add(bookAuthor);
		Book book = Book.create(bookName, ISBN, cost, bookAuthors);
		book.id = bookId;
		
		bookRepository.update(book);
		Book bookUpdated = bookRepository.get(bookId);
		
		assertThat(bookUpdated).isNotNull();
		assertThat(bookUpdated.bookName).isEqualTo(bookName);
		assertThat(bookUpdated.ISBN).isEqualTo(ISBN);
		assertThat(bookUpdated.bookCost.compareTo(cost)).isEqualTo(0);
	}
	
	@Test
	public void given_valid_parameters_should_delete_book() {
		Integer bookId = 10;
		
		bookRepository.delete(bookId);
		Book book = bookRepository.get(bookId);
		
		assertThat(book).isNull();
	}
}
