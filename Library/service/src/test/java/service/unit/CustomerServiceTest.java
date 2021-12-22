package service.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.CustomerDto;
import entities.Customer;
import exceptions.service.customer.CustomerFistNameCannotBeEmptyException;
import exceptions.service.customer.CustomerLastNameCannotBeEmptyException;
import exceptions.service.customer.CustomerNotFoundException;
import exceptions.service.customer.InvalidCustomerLimitException;
import interfaces.CustomerService;
import repository.CustomerRepository;
import services.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

	private CustomerService customerService;
	private CustomerRepository customerRepository;
	
	@Before 
	public void init() {
		customerRepository = Mockito.mock(CustomerRepository.class);
		customerService = new CustomerServiceImpl(customerRepository);
    }
	
	@Test
	public void given_valid_parameters_should_add_customer() {
		CustomerDto customer = createCustomer();
		
		customerService.add(customer);
				
		verify(customerRepository).add(any(Customer.class));
	}
	
	@Test
	public void given_invalid_first_name_when_add_should_add_customer() {
		String firstName = "";
		String lastName = "Last";
		CustomerDto customer = createCustomer(firstName, lastName);
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
		CustomerDto customer = createCustomer(firstName, lastName);
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
		String lastNameUpdated = "LastModified";
		Integer id = 1;
		Customer customer = Customer.create(firstName, lastName);
		customer.id = id;
		CustomerDto customerDto = createCustomer(firstName, lastNameUpdated);
		customerDto.id = id;
		doReturn(customer).when(customerRepository).get(id);
		
		customerService.update(customerDto);
		
		verify(customerRepository).update(any(Customer.class));
	}
	
	@Test
	public void given_invalid_first_name_when_update_should_add_customer() {
		String firstNameAfterUpdate = "";
		String lastName = "Last";
		CustomerDto customer = createCustomer(firstNameAfterUpdate, lastName);
		Integer id = 1;
		customer.id = id;
		CustomerFistNameCannotBeEmptyException expectedException = new CustomerFistNameCannotBeEmptyException(id);
		
		CustomerFistNameCannotBeEmptyException thrown = (CustomerFistNameCannotBeEmptyException) catchThrowable(() -> customerService.update(customer));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(customer.id);
	}
	
	@Test
	public void given_invalid_limit_when_update_should_add_customer() {
		String firstName = "First";
		String lastName = "Last";
		Integer limitAfterUpdate = -1;
		CustomerDto customer = createCustomer(firstName, lastName);
		Integer id = 1;
		customer.id = id;
		customer.limit = limitAfterUpdate;
		InvalidCustomerLimitException expectedException = new InvalidCustomerLimitException(id, limitAfterUpdate);
		
		InvalidCustomerLimitException thrown = (InvalidCustomerLimitException) catchThrowable(() -> customerService.update(customer));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(customer.id);
	}
	
	@Test
	public void given_invalid_last_name_when_update_should_add_customer() {
		String firstName = "First";
		String lastNameAfterUpdate = "";
		CustomerDto customer = createCustomer(firstName, lastNameAfterUpdate);
		Integer id = 1;
		customer.id = id;
		CustomerLastNameCannotBeEmptyException expectedException = new CustomerLastNameCannotBeEmptyException(id);
		
		CustomerLastNameCannotBeEmptyException thrown = (CustomerLastNameCannotBeEmptyException) catchThrowable(() -> customerService.update(customer));				
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.customerId).isEqualTo(customer.id);
	}
	
	@Test
	public void given_valid_parameters_should_delete_customer() {
		String firstName = "First";
		String lastName = "Last";
		Customer customer = Customer.create(firstName, lastName);
		Integer id = 1;
		customer.id = id;
		doReturn(customer).when(customerRepository).get(id);
		
		customerService.delete(id);
		
		verify(customerRepository, times(1)).delete(any(Integer.class));	
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
		CustomerDto customerDto = createCustomer(firstName, lastName);
		Customer customer = Customer.create(firstName, lastName);
		customerDto.canBorrow = false;
		Integer id = 1;
		customer.canBorrow = false;
		boolean expectedCanBorrow = false;
		doReturn(customer).when(customerRepository).get(id);
		
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
	
	private CustomerDto createCustomer() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.firstName = "Mr";
		customerDto.lastName = "WiseGuy";
		customerDto.limit = 0;
		customerDto.canBorrow = true;
		
		return customerDto;
	}
	
	private CustomerDto createCustomer(String firstName, String lastName) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.firstName = firstName;
		customerDto.lastName = lastName;
		customerDto.limit = 0;
		customerDto.canBorrow = true;
		
		return customerDto;
	}
}
