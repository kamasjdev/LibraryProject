package tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import services.CustomerService;

public class CustomerServiceTests {
	private CustomerService customerService;
	
	public CustomerServiceTests() {
		customerService = new CustomerService();
	}
}
