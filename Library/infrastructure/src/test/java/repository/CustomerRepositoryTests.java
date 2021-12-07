package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import entities.Customer;
import interfaces.CustomerRepository;

public class CustomerRepositoryTests {
	private CustomerRepository customerRepository; 
	
	public CustomerRepositoryTests() {
		SessionManager.createSessionManager("hibernateTest.cfg.xml");
		customerRepository = new CustomerRepositoryImpl(SessionManager.getSessionFactory());
	}
	
	@Test
	public void given_valid_parameters_should_return_list_customer() {
		List<Customer> customers = customerRepository.getAll();
		
		assertThat(customers).isNotNull();
		assertThat(customers.size()).isGreaterThan(0);
	}
	
	@Test
	public void given_valid_parameters_should_add_customer() {
		String firstName = "Tester1";
		String lastName = "Testowy";
		Customer customer = Customer.create(firstName, lastName);
		
		Integer id = customerRepository.add(customer);
		
		assertThat(id).isNotNull();
	}
	
	@Test
	public void given_valid_parameters_should_return_customer() {
		Integer customerId = 7;
		
		Customer customer = customerRepository.get(customerId);
		
		assertThat(customer).isNotNull();
	}
	
	@Test
	public void given_valid_parameters_should_update_customer() {
		String firstName = "Nameless";
		Integer customerId = 7;
		Customer customer = customerRepository.get(customerId);
		customer.person.firstName = firstName;
		
		customerRepository.update(customer);
		Customer customerUpdated = customerRepository.get(customerId);
		
		assertThat(customerUpdated).isNotNull();
		assertThat(customerUpdated.person.firstName).isEqualTo(firstName);
	}
	
	@Test
	public void given_valid_parameters_should_delete_customer() {
		Integer customerId = 7;
		
		customerRepository.delete(customerId);
		Customer customer = customerRepository.get(customerId);
		
		assertThat(customer).isNull();
	}
}