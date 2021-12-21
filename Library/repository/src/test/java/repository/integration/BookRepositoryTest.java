package repository.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import entities.Book;
import repository.BookRepository;
import repository.configuration.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Transactional
@Sql({"classpath:init.sql"})
public class BookRepositoryTest {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Test
	public void given_valid_id_should_return_book_from_db() {
		Integer id = 2;
		
		Book book = bookRepository.get(id);
		
		assertThat(book).isNotNull();
		assertThat(book.id).isEqualTo(id);
	}
	
	@Test
	public void given_book_should_add_to_db() {
		String bookName = "Java";
		String ISBN = "1254645";
		BigDecimal bookCost = new BigDecimal(200);
		Book book = Book.create(bookName, ISBN, bookCost);
		
		Integer id = bookRepository.add(book);
		
		Book bookFromDb = bookRepository.get(id);
		assertThat(bookFromDb).isNotNull();
		assertThat(bookFromDb.id).isEqualTo(id);
	}
	
	@Test
	public void given_books_from_init_should_return_more_than_one_book() {
		int size = 3;
		
		List<Book> books = bookRepository.getAll();
		
		assertThat(books.size()).isGreaterThan(0);
		assertThat(books.size()).isEqualTo(size);
	}
	
	@Test
	public void given_valid_book_should_delete_from_db() {
		String bookName = "Java";
		String ISBN = "1254645";
		BigDecimal bookCost = new BigDecimal(200);
		Book book = Book.create(bookName, ISBN, bookCost);
		Integer id = bookRepository.add(book);
		
		bookRepository.delete(id);
		
		Book bookFromDb = bookRepository.get(id);
		assertThat(bookFromDb).isNull();
	}
	
	@Test
	public void given_valid_book_should_update() {
		Integer id = 1;
		String bookName = "Java Programminmg";
		Book book = bookRepository.get(id);
		book.bookName = bookName;
		
		bookRepository.update(book);
		
		Book bookUpdated = bookRepository.get(id);
		assertThat(bookUpdated.bookName).isEqualTo(bookName);
	}
}
