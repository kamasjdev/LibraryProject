package services;

import java.util.ArrayList;
import java.util.List;

import dto.BillDto;
import dto.BookCustomerDto;
import dto.CustomerDto;
import entities.Bill;
import entities.BookCustomer;
import entities.Customer;
import exceptions.service.customer.CustomerCannotBeNullException;
import exceptions.service.customer.CustomerFistNameCannotBeEmptyException;
import exceptions.service.customer.CustomerLastNameCannotBeEmptyException;
import exceptions.service.customer.CustomerNotFoundException;
import exceptions.service.customer.InvalidCustomerLimitException;
import helpers.services.mappings.Mapper;
import interfaces.BaseService;
import interfaces.CustomerRepository;
import interfaces.CustomerService;

public class CustomerServiceImpl implements CustomerService {
	private final CustomerRepository customerRepository;
	
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@Override
	public CustomerDto getById(Integer id) {
		Customer customer = customerRepository.get(id);
		
		if(customer == null) {
			return null;
		}
		
		CustomerDto customerDto = Mapper.mapToCustomerDto(customer);
		return customerDto;
	}

	@Override
	public List<CustomerDto> getAll() {
		List<Customer> customers = customerRepository.getAll();
		List<CustomerDto> customersDto = new ArrayList<CustomerDto>();
		
		for(Customer customer : customers) {
			CustomerDto customerDto = Mapper.mapToCustomerDto(customer);
			customersDto.add(customerDto);
		}
		
		return customersDto;
	}

	@Override
	public void update(CustomerDto dto) {
		validateCustomer(dto);
		
		CustomerDto customerDto = getById(dto.id);
		
		if(customerDto == null) {
			throw new CustomerNotFoundException(dto.id);
		}
		
		customerDto.limit = dto.limit;
		customerDto.firstName = dto.firstName;
		customerDto.lastName = dto.lastName;
		Customer customer = Mapper.mapToCustomer(customerDto);
		customerRepository.update(customer);
	}

	@Override
	public Integer add(CustomerDto dto) {
		validateCustomer(dto);
		Customer customer = Customer.create(dto.firstName, dto.lastName);
		
		Integer id = customerRepository.add(customer);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		CustomerDto customerDto = getById(id);
		
		if(customerDto == null) {
			throw new CustomerNotFoundException(id);
		}
		
		customerRepository.delete(id);
	}

	private void validateCustomer(CustomerDto customerDto) {
		if(customerDto == null) {
			throw new CustomerCannotBeNullException();
		}
		
		if(customerDto.firstName == null) {
			throw new CustomerFistNameCannotBeEmptyException(customerDto.id);
		}
		
		if(customerDto.firstName.isEmpty()) {
			throw new CustomerFistNameCannotBeEmptyException(customerDto.id);
		}
		
		if(customerDto.lastName == null) {
			throw new CustomerLastNameCannotBeEmptyException(customerDto.id);
		}
		
		if(customerDto.lastName.isEmpty()) {
			throw new CustomerLastNameCannotBeEmptyException(customerDto.id);
		}
		
		if(customerDto.limit == null) {
			throw new InvalidCustomerLimitException(customerDto.id, customerDto.limit);
		}
		
		if(customerDto.limit < 0) {
			throw new InvalidCustomerLimitException(customerDto.id, customerDto.limit);
		}
	}

	public boolean canBorrow(Integer customerId) {
		CustomerDto customerDto = getById(customerId);
		
		if(customerDto == null) {
			throw new CustomerNotFoundException(customerId);
		}
		
		boolean canBorrow = customerDto.canBorrow;
		return canBorrow;
	}

	public CustomerDto getDetails(Integer customerId) {
		Customer customer = customerRepository.getDetails(customerId);
		
		if(customer == null) {
			return null;
		}
		
		CustomerDto customerDto = Mapper.mapToCustomerDto(customer);
		
		for(BookCustomer bookCustomer : customer.books) {
			BookCustomerDto bookCustomerDto = Mapper.mapToBookCustomerDto(bookCustomer);
			bookCustomerDto.book = Mapper.mapToBookDto(bookCustomer.book);
			customerDto.books.add(bookCustomerDto);
		}
		
		for(Bill bill : customer.bills) {
			BillDto billDto = Mapper.mapToBillDto(bill);
			customerDto.bills.add(billDto);			
		}
		
		return customerDto;
	}	
}