package entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import common.BaseEntity;
import common.Person;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -201003798683894921L;

	public Customer() {
		person = new Person();
		books = new HashSet<BookCustomer>();
		bills = new HashSet<Bill>();
	}
	
	@Embedded
	@AttributeOverrides({
	  @AttributeOverride( name = "firstName", column = @Column(name = "first_name")),
	  @AttributeOverride( name = "lastName", column = @Column(name = "last_name"))
	})
	public Person person;
	
	@Column(name = "books_limit", nullable = false)
	public Integer limit;
	
	@Column(name = "can_borrow")
	public boolean canBorrow;
	
	@OneToMany(mappedBy = "customer")
	public Set<BookCustomer> books;
	
	@OneToMany(mappedBy = "customer")
	public Set<Bill> bills;
	
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
