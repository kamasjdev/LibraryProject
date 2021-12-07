package managers;

import java.util.List;

import dto.CustomerDto;
import services.BillService;
import services.CustomerService;

public class CustomerManager {
	private final CustomerService customerService;
	private final BillService billService;
	
	public CustomerManager(CustomerService customerService, BillService billService) {
		this.customerService = customerService;
		this.billService = billService;
	}

	public Integer addCustomer(String firstName, String lastName) {		
		CustomerDto customerDto = new CustomerDto();
		customerDto.firstName = firstName;
		customerDto.lastName = lastName;
		customerDto.limit = 0;
		Integer id = customerService.add(customerDto);
		return id;
	}

	public List<CustomerDto> getAll() {
		List<CustomerDto> customersDto = customerService.getEntities();
		return customersDto;
	}

	public void getCustomerDetails(Integer customerId) {
		CustomerDto customerDto = customerService.getDetails(customerId);
		
		if(customerDto == null) {
			System.out.println("Customer not found");
			return;
		}
		
		System.out.println("Cutomer: ");
		System.out.println(customerDto);
	}

	public void deleteCustomer(Integer customerId, String forceDelete) {
		CustomerDto customerDto = customerService.getDetails(customerId);
		
		if(customerDto == null) {
			System.out.println("Customer not found");
			return;
		}
		
		if(!customerDto.books.isEmpty()) {
			System.out.println("Cannot delete customer. First return books");
			return;
		}
		
		if(forceDelete.equals("Y")) {
			billService.deleteAllBillsByCustomerId(customerId);
		}
		
		customerService.delete(customerId);
	}

	public void editCustomer(Integer customerId, String firstName, String lastName, Integer limit, String borrow) {
		CustomerDto customerDto = customerService.getById(customerId);
		
		if(customerDto == null) {
			System.out.println("Customer not found");
			return;
		}
		
		if(!firstName.isEmpty()) {
			customerDto.firstName = firstName;			
		}
		
		if(!lastName.isEmpty()) {
			customerDto.lastName = lastName;			
		}
		
		if(limit != -1) {
			customerDto.limit = limit;
		}

		if(borrow.equals("Y")) {
			customerDto.canBorrow = true;
		} else if (borrow.equals("N")) {
			customerDto.canBorrow = false;
		}
		
		customerService.update(customerDto);
	}
}