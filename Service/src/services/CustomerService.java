package services;

import java.util.List;

import abstracts.AbstractBaseService;
import entities.Customer;
import exceptions.service.customer.CustomerCannotBeNullException;
import exceptions.service.customer.CustomerFistNameCannotBeEmptyException;
import exceptions.service.customer.CustomerLastNameCannotBeEmptyException;
import exceptions.service.customer.CustomerNotFoundException;
import exceptions.service.customer.InvalidCustomerLimitException;

public class CustomerService extends AbstractBaseService<Customer> {
	
	@Override
	public Customer getById(Integer id) {
		Customer customer = objects.stream().filter(o->o.id.equals(id)).findFirst().orElse(null);
		return customer;
	}

	@Override
	public List<Customer> getEntities() {
		return objects;
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
	}

	@Override
	public Integer add(Customer entity) {
		validateCustomer(entity);
		
		Integer id = getLastId();
		entity.id = id;
		objects.add(entity);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		Customer customer = getById(id);
		
		if(customer == null) {
			throw new CustomerNotFoundException(id);
		}
		
		objects.remove(customer);
	}

	private void validateCustomer(Customer customer) {
		if(customer == null) {
			throw new CustomerCannotBeNullException();
		}
		
		if(customer.person.firstName.isEmpty()) {
			throw new CustomerFistNameCannotBeEmptyException(customer.id);
		}
		
		if(customer.person.lastName.isEmpty()) {
			throw new CustomerLastNameCannotBeEmptyException(customer.id);
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
