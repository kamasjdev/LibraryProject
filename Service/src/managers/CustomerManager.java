package managers;

import java.util.List;

import entities.Customer;
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
		Customer customer = Customer.create(firstName, lastName);
		Integer id = customerService.add(customer);
		return id;
	}

	public List<Customer> getAll() {
		List<Customer> customers = customerService.getEntities();
		return customers;
	}

	public void getCustomerDetails(Integer customerId) {
		Customer customer = customerService.getDetails(customerId);
		
		if(customer == null) {
			System.out.println("Customer not found");
			return;
		}
		
		System.out.println("Cutomer: ");
		System.out.println(customer);
	}

	public void deleteCustomer(Integer customerId, String forceDelete) {
		Customer customer = customerService.getDetails(customerId);
		
		if(customer == null) {
			System.out.println("Customer not found");
			return;
		}
		
		if(!customer.books.isEmpty()) {
			System.out.println("Cannot delete customer. First return books");
			return;
		}
		
		if(forceDelete.equals("Y")) {
			billService.deleteAllBillsByCustomerId(customerId);
		}
		
		customerService.delete(customerId);
	}

	public void editCustomer(Integer customerId, String firstName, String lastName, Integer limit, String borrow) {
		Customer customer = customerService.getById(customerId);
		
		if(customer == null) {
			System.out.println("Customer not found");
			return;
		}
		
		if(!firstName.isEmpty()) {
			customer.person.firstName = firstName;			
		}
		
		if(!lastName.isEmpty()) {
			customer.person.lastName = lastName;			
		}
		
		if(limit != -1) {
			customer.limit = limit;
		}

		if(borrow.equals("Y")) {
			customer.canBorrow = true;
		} else if (borrow.equals("N")) {
			customer.canBorrow = false;
		}
		
		customerService.update(customer);
	}
}
