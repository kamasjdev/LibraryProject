package entities;

import java.util.HashSet;

import common.BaseEntity;
import common.Person;

public class Customer extends BaseEntity {
	public Customer() {
		person = new Person();
		books = new HashSet<BookCustomer>();
	}
	
	public Person person;
	public Integer limit;
	public boolean canBorrow;
	
	public HashSet<BookCustomer> books;
	
	public static Customer Create(String firstName, String lastName) {
		Customer customer = new Customer();
		customer.person.firstName = firstName;
		customer.person.lastName = lastName;
		customer.limit = 0;
		
		return customer;
	}
	
	public static Customer Create(String firstName, String lastName, HashSet<BookCustomer> books) {
		Customer customer = new Customer();
		customer.person.firstName = firstName;
		customer.person.lastName = lastName;
		customer.limit = 0;
		
		if(!books.isEmpty()) {
			customer.books = books;
		}
		
		return customer;
	}
	
	@Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s %4$s", id, person.firstName, person.lastName, limit);
		return description;
	}
}
