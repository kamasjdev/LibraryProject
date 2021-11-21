package managers;

import java.util.List;
import java.util.stream.Collectors;

import entities.Bill;
import entities.Book;
import entities.Customer;
import interfaces.ActionService;
import services.BillService;
import services.BookService;
import services.CustomerService;

public class CustomerManager {
	private CustomerService customerService;
	private BillService billService;
	private ActionService actionService;
	private BookService bookService;
	
	public CustomerManager(CustomerService customerService, BillService billService, BookService bookService, ActionService actionService) {
		this.customerService = customerService;
		this.billService = billService;
		this.actionService = actionService;
		this.bookService = bookService;
	}

	public Integer addCustomer() {		
		System.out.println("Please enter first name for customer");
		String firstName = actionService.inputLine(String.class);
		System.out.println("Please enter last name for customer");
		String lastName = actionService.inputLine(String.class);
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
		System.out.println("Cutomer's books: ");
		books.forEach(b->System.out.println(b));
		List<Bill> bills = billService.getEntities().stream().filter(c->c.getCustomerId()==customerId).collect(Collectors.toList());
		
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

	public void editCustomer(Integer customerId) {
		Customer customer = customerService.getById(customerId);
		
		if(customer == null) {
			System.out.println("Customer not found");
			return;
		}
		
		System.out.println("Enter first name, if dont need to change leave empty");
		String firstName = actionService.inputLine(String.class);
		
		if(!firstName.isEmpty()) {
			customer.person.firstName = firstName;			
		}
		
		System.out.println("Enter last name, if dont need to change leave empty");
		String lastName = actionService.inputLine(String.class);
		
		if(!lastName.isEmpty()) {
			customer.person.firstName = lastName;			
		}
		
		System.out.println("Enter limit for borrow, if dont need to change put -1");
		Integer limit = actionService.inputLine(Integer.class);
		
		if(limit != -1) {
			customer.limit = limit;
		}
				
		System.out.println("Set allow borrow book for customer, Y to yes, N to no");
		String borrow = actionService.inputLine(String.class);
		
		if(borrow.equals("Y")) {
			customer.canBorrow = true;
		} else if (borrow.equals("N")) {
			customer.canBorrow = false;
		}
		
		customerService.update(customer);
	}
}
