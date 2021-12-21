package repository.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import entities.BookAuthor;
import repository.BookAuthorRepository;
import repository.configuration.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Transactional
@Sql({"classpath:init.sql"})
public class BookAuthorRepositoryTest {
	
	@Autowired
	private BookAuthorRepository bookAuthorRepository;
	
	@Test
	public void given_valid_id_should_return_bookCustomer_from_db() {
		Integer id = 2;
		
		BookAuthor bookCustomer = bookAuthorRepository.get(id);
		
		assertThat(bookCustomer).isNotNull();
		assertThat(bookCustomer.id).isEqualTo(id);
	}
	
	@Test
	public void given_bookCustomer_should_add_to_db() {
		Integer bookId = 2;
		Integer authorId = 2;
		BookAuthor bookCustomer = BookAuthor.create(bookId, authorId);
		
		Integer id = bookAuthorRepository.add(bookCustomer);
		
		BookAuthor bookCustomerFromDb = bookAuthorRepository.get(id);
		assertThat(bookCustomerFromDb).isNotNull();
		assertThat(bookCustomerFromDb.id).isEqualTo(id);
	}
	
	@Test
	public void given_bookCustomers_from_init_should_return_more_than_one_bookCustomer() {
		int size = 4;
		
		List<BookAuthor> bookCustomers = bookAuthorRepository.getAll();
		
		assertThat(bookCustomers.size()).isGreaterThan(0);
		assertThat(bookCustomers.size()).isEqualTo(size);
	}
	
	@Test
	public void given_valid_bookCustomer_should_delete_from_db() {
		Integer bookId = 2;
		Integer authorId = 2;
		BookAuthor bookCustomer = BookAuthor.create(bookId, authorId);		
		Integer id = bookAuthorRepository.add(bookCustomer);
		
		bookAuthorRepository.delete(id);
		
		BookAuthor bookCustomerFromDb = bookAuthorRepository.get(id);
		assertThat(bookCustomerFromDb).isNull();
	}
	
	@Test
	public void given_valid_bookCustomer_should_update() {
		Integer id = 4;
		Integer bookId = 1;
		BookAuthor bookCustomer = bookAuthorRepository.get(id);
		bookCustomer.bookId = bookId;
		
		bookAuthorRepository.update(bookCustomer);
		
		BookAuthor bookCustomerUpdated = bookAuthorRepository.get(id);
		assertThat(bookCustomerUpdated.bookId).isEqualTo(bookId);
	}
}
