package services;

import java.util.List;

import entities.Customer;
import exceptions.service.customer.CustomerCannotBeNullException;
import exceptions.service.customer.CustomerFistNameCannotBeEmptyException;
import exceptions.service.customer.CustomerLastNameCannotBeEmptyException;
import exceptions.service.customer.CustomerNotFoundException;
import exceptions.service.customer.InvalidCustomerLimitException;
import interfaces.BaseService;
import interfaces.CustomerRepository;

public class CustomerService implements BaseService<Customer> {
	private final CustomerRepository customerRepository;
	
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@Override
	public Customer getById(Integer id) {
		Customer customer = customerRepository.get(id);
		return customer;
	}

	@Override
	public List<Customer> getEntities() {
		List<Customer> customers = customerRepository.getAll();
		return customers;
	}

	@Override
	public void update(Customer entity) {
		validateCustomer(entity);
		
		Customer customer = getById(entity.id);
		
		if(customer == null) {
			throw new CustomerNotFoundException(entity.id);
		}
		
		customer.limit = entity.limit;
		customer.person.firstName = entity.person.firstName;
		customer.person.lastName = entity.person.lastName;
		customerRepository.update(customer);
	}

	@Override
	public Integer add(Customer entity) {
		validateCustomer(entity);
		
		Integer id = customerRepository.add(entity);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		Customer customer = getById(id);
		
		if(customer == null) {
			throw new CustomerNotFoundException(id);
		}
		
		customerRepository.delete(customer);
	}

	private void validateCustomer(Customer customer) {
		if(customer == null) {
			throw new CustomerCannotBeNullException();
		}
		
		if(customer.person.firstName == null) {
			throw new CustomerFistNameCannotBeEmptyException(customer.id);
		}
		
		if(customer.person.firstName.isEmpty()) {
			throw new CustomerFistNameCannotBeEmptyException(customer.id);
		}
		
		if(customer.person.lastName == null) {
			throw new CustomerLastNameCannotBeEmptyException(customer.id);
		}
		
		if(customer.person.lastName.isEmpty()) {
			throw new CustomerLastNameCannotBeEmptyException(customer.id);
		}
		
		if(customer.limit == null) {
			throw new InvalidCustomerLimitException(customer.id, customer.limit);
		}
		
		if(customer.limit < 0) {
			throw new InvalidCustomerLimitException(customer.id, customer.limit);
		}
	}

	public boolean canBorrow(Integer customerId) {
		Customer customer = getById(customerId);
		
		if(customer == null) {
			throw new CustomerNotFoundException(customerId);
		}
		
		boolean canBorrow = customer.canBorrow;
		return canBorrow;
	}	
}
