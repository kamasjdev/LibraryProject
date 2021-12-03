package tests.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import entities.Customer;
import exceptions.service.customer.CustomerFistNameCannotBeEmptyException;
import exceptions.service.customer.CustomerLastNameCannotBeEmptyException;
import exceptions.service.customer.CustomerNotFoundException;
import exceptions.service.customer.InvalidCustomerLimitException;
import interfaces.CustomerRepository;
import services.CustomerService;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTests {
	private CustomerService customerService;
	private CustomerRepository customerRepository;
	
	public CustomerServiceTests() {
		customerRepository = Mockito.mock(CustomerRepository.class); 
		customerService = new CustomerService(customerRepository);
	}
	
	@Test
	public void given_valid_parameters_should_add_customer() {
		String firstName = "First";
		String lastName = "Last";
		Customer customer = Customer.create(firstName, lastName);
		Integer expectedId = 1;
		
		Integer id = customerService.add(customer);
				
		assertThat(id).isEqualTo(expectedId);
	}
	
	@Test
	public void given_invalid_first_name_when_add_should_add_customer() {
		String firstName = "";
		String lastName = "Last";
		Customer customer = Customer.create(firstName, lastName);
		CustomerFistNameCannotBeEmptyException expectedException = new CustomerFistNameCannotBeEmptyException(customer.id);
		
		CustomerFistNameCannotBeEmptyException thrown = (CustomerFistNameCannotBeEmptyException) catchThrowable(() -> customerService.add(customer));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(customer.id);
	}
	
	@Test
	public void given_invalid_last_name_when_add_should_add_customer() {
		String firstName = "First";
		String lastName = "";
		Customer customer = Customer.create(firstName, lastName);
		CustomerLastNameCannotBeEmptyException expectedException = new CustomerLastNameCannotBeEmptyException(customer.id);
		
		CustomerLastNameCannotBeEmptyException thrown = (CustomerLastNameCannotBeEmptyException) catchThrowable(() -> customerService.add(customer));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(customer.id);
	}
	
	@Test
	public void given_valid_parameters_should_update_customer() {
		String firstName = "First";
		String lastName = "Last";
		Customer customer = Customer.create(firstName, lastName);
		Integer id = customerService.add(customer);
		Integer expectedLimit = 2;
		String expectedName = "Name";
		
		Customer customerAdded = customerService.getById(id);
		customerAdded.limit = expectedLimit;
		customerAdded.person.firstName = expectedName;
		customerService.update(customerAdded);
		Customer customerUpdated = customerService.getById(id);
		
		assertThat(customerUpdated.limit).isEqualTo(expectedLimit);
		assertThat(customerUpdated.person.firstName).isEqualTo(expectedName);
	}
	
	@Test
	public void given_invalid_first_name_when_update_should_add_customer() {
		String firstName = "First";
		String firstNameAfterUpdate = "";
		String lastName = "Last";
		Customer customer = Customer.create(firstName, lastName);
		Integer id = customerService.add(customer);
		CustomerFistNameCannotBeEmptyException expectedException = new CustomerFistNameCannotBeEmptyException(id);
		
		Customer customerAdded = customerService.getById(id);
		customerAdded.person.firstName = firstNameAfterUpdate;
		CustomerFistNameCannotBeEmptyException thrown = (CustomerFistNameCannotBeEmptyException) catchThrowable(() -> customerService.update(customerAdded));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(customer.id);
	}
	
	@Test
	public void given_invalid_limit_when_update_should_add_customer() {
		String firstName = "First";
		String lastName = "Last";
		Integer limitAfterUpdate = -1;
		Customer customer = Customer.create(firstName, lastName);
		Integer id = customerService.add(customer);
		InvalidCustomerLimitException expectedException = new InvalidCustomerLimitException(id, limitAfterUpdate);
		
		Customer customerAdded = customerService.getById(id);
		customerAdded.limit = limitAfterUpdate;
		InvalidCustomerLimitException thrown = (InvalidCustomerLimitException) catchThrowable(() -> customerService.update(customerAdded));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(customer.id);
	}
	
	@Test
	public void given_invalid_last_name_when_update_should_add_customer() {
		String firstName = "First";
		String lastName = "Last";
		String lastNameAfterUpdate = "";
		Customer customer = Customer.create(firstName, lastName);
		Integer id = customerService.add(customer);
		CustomerLastNameCannotBeEmptyException expectedException = new CustomerLastNameCannotBeEmptyException(id);
		
		Customer customerAdded = customerService.getById(id);
		customerAdded.person.lastName = lastNameAfterUpdate;
		CustomerLastNameCannotBeEmptyException thrown = (CustomerLastNameCannotBeEmptyException) catchThrowable(() -> customerService.update(customerAdded));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(customer.id);
	}
	
	@Test
	public void given_valid_parameters_should_delete_customer() {
		String firstName = "First";
		String lastName = "Last";
		Customer customer = Customer.create(firstName, lastName);
		Integer id = customerService.add(customer);
		int expectedSize = 0;
		
		customerService.delete(id);
		List<Customer> customers = customerService.getEntities();
		
		assertThat(customers.size()).isEqualByComparingTo(expectedSize);	
	}
	
	@Test
	public void given_invalid_id_when_delete_should_throw_an_exception() {
		Integer id = 1;
		CustomerNotFoundException expectedException = new CustomerNotFoundException(id);
		
		CustomerNotFoundException thrown = (CustomerNotFoundException) catchThrowable(() -> customerService.delete(id));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(id);
	}
	
	@Test
	public void given_valid_parameters_should_return_that_customer_cant_borrow() {
		String firstName = "First";
		String lastName = "Last";
		Customer customer = Customer.create(firstName, lastName);
		Integer id = customerService.add(customer);
		Customer CustomerAdded = customerService.getById(id);
		CustomerAdded.canBorrow = false;
		customerService.update(CustomerAdded);
		boolean expectedCanBorrow = false;
		
		boolean canBorrow = customerService.canBorrow(id);
		
		assertThat(canBorrow).isEqualTo(expectedCanBorrow);
	}
	
	@Test
	public void given_invalid_id_when_check_if_customer_can_borrow_should_throw_an_exception() {
		Integer id = 100;
		CustomerNotFoundException expectedException = new CustomerNotFoundException(id);
		
		CustomerNotFoundException thrown = (CustomerNotFoundException) catchThrowable(() -> customerService.canBorrow(id));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(id);
	}
}
