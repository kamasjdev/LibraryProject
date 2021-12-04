package entities;

import java.util.HashSet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.BaseEntity;
import common.Person;

public class Customer extends BaseEntity {
	public Customer() {
		person = new Person();
		books = new HashSet<BookCustomer>();
		bills = new HashSet<Bill>();
	}
	
	public Person person;
	public Integer limit;
	public boolean canBorrow;
	
	@JsonDeserialize(as = HashSet.class)
	@JsonSerialize(as = HashSet.class)
	public HashSet<BookCustomer> books;
	
	@JsonDeserialize(as = HashSet.class)
	@JsonSerialize(as = HashSet.class)
	public HashSet<Bill> bills;
	
	public static Customer create(String firstName, String lastName) {
		Customer customer = new Customer();
		customer.person.firstName = firstName;
		customer.person.lastName = lastName;
		customer.limit = 0;
		customer.canBorrow = true;
		
		return customer;
	}
	
	public static Customer create(String firstName, String lastName, HashSet<BookCustomer> books) {
		Customer customer = new Customer();
		customer.person.firstName = firstName;
		customer.person.lastName = lastName;
		customer.limit = 0;
		customer.canBorrow = true;
		
		if(!books.isEmpty()) {
			customer.books = books;
		}
		
		return customer;
	}
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder().append(id).append(". ").append(person.firstName)
				.append(" ").append(person.lastName).append(" ").append(limit).append(" ").append(canBorrow);
		
		if(!books.isEmpty()) {
			description.append("\nBooks borrowed by customer:\n");
			
			for(BookCustomer bookCustomer : books) {
				description.append(bookCustomer);
				description.append("\n");
			}
		}
		
		if(!bills.isEmpty()) {
			description.append("\nCustomer bills:\n");
			
			for(Bill bill : bills) {
				description.append(bill);
				description.append("\n");
			}
		}
		
		return description.toString();
	}
}
