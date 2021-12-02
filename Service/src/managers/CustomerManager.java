package managers;

import java.util.List;
import java.util.stream.Collectors;

import entities.Bill;
import entities.Book;
import entities.Customer;
import services.BillService;
import services.BookService;
import services.CustomerService;

public class CustomerManager {
	private final CustomerService customerService;
	private final BillService billService;
	private final BookService bookService;
	
	public CustomerManager(CustomerService customerService, BillService billService, BookService bookService) {
		this.customerService = customerService;
		this.billService = billService;
		this.bookService = bookService;
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
		Customer customer = customerService.getById(customerId);
		
		if(customer == null) {
			System.out.println("Customer not found");
			return;
		}
		
		List<Integer> bookList = customer.books.stream().map(b->b.bookId).collect(Collectors.toList());
		List<Book> books = bookService.getEntities().stream().filter(b->bookList.stream().anyMatch(bl->bl.equals(b.id))).collect(Collectors.toList());
		System.out.println("Cutomer: ");
		System.out.println(customer);
		
		if(books.isEmpty()) {
			return;
		}
		
		System.out.println("Cutomer's books: ");
		books.forEach(b->System.out.println(b));
		List<Bill> bills = billService.getEntities().stream().filter(c->c.customerId.equals(customerId)).collect(Collectors.toList());
		
		if(bills.isEmpty()) {
			return;
		}
		
		System.out.println("Cutomer's bills: ");
		bills.forEach(b-> System.out.println(b));
	}

	public void deleteCustomer(Integer customerId) {
		Customer customer = customerService.getById(customerId);
		
		if(customer == null) {
			System.out.println("Customer not found");
			return;
		}
		
		if(!customer.books.isEmpty()) {
			System.out.println("Cannot delete customer. First return books");
			return;
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
			customer.person.firstName = lastName;			
		}
		
		if(limit != -1) {
			customer.limit = limit;
		}
				
		System.out.println("Set allow borrow book for customer, Y to yes, N to no");

		if(borrow.equals("Y")) {
			customer.canBorrow = true;
		} else if (borrow.equals("N")) {
			customer.canBorrow = false;
		}
		
		customerService.update(customer);
	}
}
