package tests.integration;

import entities.Book;
import entities.BookCustomer;
import entities.Customer;
import interfaces.CustomerRepository;
import interfaces.MapEntity;
import mappings.BookCustomerMapping;
import mappings.BookMapping;
import mappings.CustomerMapping;
import repository.CustomerRepositoryImpl;

public class CustomerRepositoryTests extends BaseTest {
	private CustomerRepository customerRepository;
	private MapEntity<Customer> mapper;
	private MapEntity<BookCustomer> bookCustomerMapper;
	private MapEntity<Book> bookMapper;
	
	public CustomerRepositoryTests() {
		mapper = new CustomerMapping();
		bookCustomerMapper = new BookCustomerMapping();
		bookMapper = new BookMapping();
		customerRepository = new CustomerRepositoryImpl(dbClient, mapper, bookCustomerMapper, bookMapper);
	}
}
