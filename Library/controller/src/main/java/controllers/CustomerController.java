package controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import controllers.mapper.MapToDto;
import controllers.pojos.customer.AddCustomer;
import controllers.pojos.customer.UpdateCustomer;
import dto.CustomerDto;
import interfaces.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookCustomerController.class);
	private final CustomerService customerService;
	
	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@GetMapping("{customerId}")
	public CustomerDto getCustomer(@PathVariable Integer customerId) {
		logger.info(String.format("Getting customer by id: %1$s", customerId));
		CustomerDto customerDto = customerService.getById(customerId);
		return customerDto;
	}
	
	@GetMapping
	public List<CustomerDto> getAll() {
		logger.info("Getting customers");
		List<CustomerDto> customers = customerService.getAll();
		return customers;
	}
	
	@PostMapping
	public int addCustomer(@RequestBody AddCustomer addCustomer) {
		logger.info("Adding new customer");
		CustomerDto customerDto = MapToDto.mapAddCustomerToCustomerDto(addCustomer);
		Integer id = customerService.add(customerDto);
		return id;
	}
	
	@PutMapping
	public void updateCustomer(@RequestBody UpdateCustomer updateCustomer) {
		logger.info("Updating customer");
		CustomerDto customerDto = MapToDto.mapUpdateCustomerToCustomerDto(updateCustomer);
		customerService.update(customerDto);
	}
	
	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable int customerId) {
		logger.info(String.format("Deleting customer with id: %1$s", customerId));
		customerService.delete(customerId);
	}
	
	@GetMapping("{customerId}/details")
	public CustomerDto getCustomerDetails(@PathVariable int customerId) {
		logger.info(String.format("Getting customer %1$s details", customerId));
		CustomerDto customerDto = customerService.getDetails(customerId);
		return customerDto;
	}
}
