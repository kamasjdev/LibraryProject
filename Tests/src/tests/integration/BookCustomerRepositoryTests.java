package tests.integration;

import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import entities.Book;
import entities.BookCustomer;
import entities.Customer;
import interfaces.BookCustomerRepository;
import interfaces.MapEntity;
import mappings.BookCustomerMapping;
import mappings.BookMapping;
import mappings.CustomerMapping;
import repository.BookCustomerRepositoryImpl;

public class BookCustomerRepositoryTests extends BaseTest {
	private BookCustomerRepository bookCustomerRepository;
	private MapEntity<BookCustomer> bookCustomerMapping;
	private MapEntity<Book> bookMapping;
	private MapEntity<Customer> customerMapping;
	
	public BookCustomerRepositoryTests() {
		this.bookCustomerMapping = new BookCustomerMapping();
		this.bookMapping = new BookMapping();
		this.customerMapping = new CustomerMapping();
		this.bookCustomerRepository = new BookCustomerRepositoryImpl(dbClient, bookCustomerMapping, bookMapping, customerMapping);
	}
	
	@Test
	public void given_valid_parameters_should_return_book_customers() {
		List<BookCustomer> bookCustomers = bookCustomerRepository.getAll();
		
		assertThat(bookCustomers).isNotNull();
		assertThat(bookCustomers.size()).isGreaterThan(0);
	}
	
	@Test
	public void given_valid_customer_id_and_book_id_should_return_book_customer() {
		Integer bookId = 1;
		Integer customerId = 3;
		
		BookCustomer bookCustomer = bookCustomerRepository.getBookCustomerByBookIdAndCustomerId(bookId, customerId);
		
		assertThat(bookCustomer).isNotNull();
	}
	
	@Test
	public void given_valid_id_should_return_book_customer() {
		Integer id = 1;
		
		BookCustomer bookCustomer = bookCustomerRepository.get(id);
		
		assertThat(bookCustomer).isNotNull();
	}
	
	@Test
	public void given_valid_parameters_should_add_book_customer() {
		Integer bookId = 6;
		Integer authorId = 3;
		BookCustomer bookCustomer = BookCustomer.create(bookId, authorId);
		
		Integer id = bookCustomerRepository.add(bookCustomer);
		
		assertThat(id).isNotNull();
	}
	
	@Test
	public void given_valid_id_should_update_book_customer() {
		Integer id = 7;
		Integer bookId = 7;
		BookCustomer bookCustomer = bookCustomerRepository.get(id);
		bookCustomer.bookId = bookId;
		
		bookCustomerRepository.update(bookCustomer);
		
		BookCustomer bookCustomerUpdated = bookCustomerRepository.get(id);
		assertThat(bookCustomerUpdated).isNotNull();
		assertThat(bookCustomerUpdated.bookId).isEqualTo(bookId);
	}
	
	@Test
	public void given_valid_id_should_delete_customer() {
		Integer id = 7;
		
		bookCustomerRepository.delete(id);
		
		BookCustomer bookCustomer = bookCustomerRepository.get(id);
		assertThat(bookCustomer).isNull();		
	}
}
