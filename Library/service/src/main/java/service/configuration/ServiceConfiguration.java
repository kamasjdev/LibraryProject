package service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import interfaces.AuthorService;
import interfaces.BillService;
import interfaces.BookAuthorService;
import interfaces.BookCustomerService;
import interfaces.BookService;
import interfaces.CustomerService;
import repository.AuthorRepository;
import repository.BillRepository;
import repository.BookAuthorRepository;
import repository.BookCustomerRepository;
import repository.BookRepository;
import repository.CustomerRepository;
import services.AuthorServiceImpl;
import services.BillServiceImpl;
import services.BookAuthorServiceImpl;
import services.BookCustomerServiceImpl;
import services.BookServiceImpl;
import services.CustomerServiceImpl;

import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(value="interfaces")
public class ServiceConfiguration {
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private BookAuthorRepository bookAuthorRepository;
	
	@Autowired
	private BookCustomerRepository bookCustomerRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private BillRepository billRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Bean
	public AuthorService authorService() {
		return new AuthorServiceImpl(authorRepository);
	}
	
	@Bean
	public BookAuthorService bookAuthorService() {
		return new BookAuthorServiceImpl(bookAuthorRepository);
	}
	
	@Bean
	public BookCustomerService bookCustomerService() {
		return new BookCustomerServiceImpl(bookCustomerRepository);
	}
	
	@Bean
	public BookService bookService() {
		return new BookServiceImpl(bookRepository);
	}
	
	@Bean
	public CustomerService customerService() {
		return new CustomerServiceImpl(customerRepository);
	}
	
	@Bean
	public BillService billService() {
		return new BillServiceImpl(billRepository);
	}
}
