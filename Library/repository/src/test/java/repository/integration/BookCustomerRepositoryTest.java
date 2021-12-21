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

import entities.BookCustomer;
import repository.BookCustomerRepository;
import repository.configuration.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Transactional
@Sql({"classpath:init.sql"})
public class BookCustomerRepositoryTest {
	
	@Autowired
	private BookCustomerRepository bookCustomerRepository;
	
	@Test
	public void given_valid_id_should_return_bookCustomer_from_db() {
		Integer id = 2;
		
		BookCustomer bookCustomer = bookCustomerRepository.get(id);
		
		assertThat(bookCustomer).isNotNull();
		assertThat(bookCustomer.id).isEqualTo(id);
	}
	
	@Test
	public void given_bookCustomer_should_add_to_db() {
		Integer bookId = 3;
		Integer customerId = 1;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		
		Integer id = bookCustomerRepository.add(bookCustomer);
		
		BookCustomer bookCustomerFromDb = bookCustomerRepository.get(id);
		assertThat(bookCustomerFromDb).isNotNull();
		assertThat(bookCustomerFromDb.id).isEqualTo(id);
	}
	
	@Test
	public void given_bookCustomers_from_init_should_return_more_than_one_bookCustomer() {
		int size = 4;
		
		List<BookCustomer> bookCustomers = bookCustomerRepository.getAll();
		
		assertThat(bookCustomers.size()).isGreaterThan(0);
		assertThat(bookCustomers.size()).isEqualTo(size);
	}
	
	@Test
	public void given_valid_bookCustomer_should_delete_from_db() {
		Integer bookId = 3;
		Integer customerId = 1;
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);		
		Integer id = bookCustomerRepository.add(bookCustomer);
		
		bookCustomerRepository.delete(id);
		
		BookCustomer bookCustomerFromDb = bookCustomerRepository.get(id);
		assertThat(bookCustomerFromDb).isNull();
	}
	
	@Test
	public void given_valid_bookCustomer_should_update() {
		Integer id = 4;
		Integer customerId = 2;
		BookCustomer bookCustomer = bookCustomerRepository.get(id);
		bookCustomer.customerId = customerId;
		
		bookCustomerRepository.update(bookCustomer);
		
		BookCustomer bookCustomerUpdated = bookCustomerRepository.get(id);
		assertThat(bookCustomerUpdated.customerId).isEqualTo(customerId);
	}
}
