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

import entities.Customer;
import repository.CustomerRepository;
import repository.configuration.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Transactional
@Sql({"classpath:init.sql"})
public class CustomerRepositoryTest {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Test
	public void given_valid_id_should_return_customer_from_db() {
		Integer id = 2;
		
		Customer customer = customerRepository.get(id);
		
		assertThat(customer).isNotNull();
		assertThat(customer.id).isEqualTo(id);
	}
	
	@Test
	public void given_customer_should_add_to_db() {
		String firstName = "Mr";
		String lastName = "Test";
		Customer customer = Customer.create(firstName, lastName);
		
		Integer id = customerRepository.add(customer);
		
		Customer customerFromDb = customerRepository.get(id);
		assertThat(customerFromDb).isNotNull();
		assertThat(customerFromDb.id).isEqualTo(id);
	}
	
	@Test
	public void given_customers_from_init_should_return_more_than_one_customer() {
		int size = 3;
		
		List<Customer> customers = customerRepository.getAll();
		
		assertThat(customers.size()).isGreaterThan(0);
		assertThat(customers.size()).isEqualTo(size);
	}
	
	@Test
	public void given_valid_customer_should_delete_from_db() {
		String firstName = "Mr";
		String lastName = "Test";
		Customer customer = Customer.create(firstName, lastName);		
		Integer id = customerRepository.add(customer);
		
		customerRepository.delete(id);
		
		Customer customerFromDb = customerRepository.get(id);
		assertThat(customerFromDb).isNull();
	}
	
	@Test
	public void given_valid_customer_should_update() {
		Integer id = 1;
		String lastName = "Tester123";
		Customer customer = customerRepository.get(id);
		customer.person.lastName = lastName;
		
		customerRepository.update(customer);
		
		Customer customerUpdated = customerRepository.get(id);
		assertThat(customerUpdated.person.lastName).isEqualTo(lastName);
	}
}
