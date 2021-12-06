package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import common.BaseEntity;

@Entity
@Table(name = "bookcustomer")
public class BookCustomer extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -8358799540384495539L;

	@Column(name = "book_id")
	public Integer bookId;
	
	@ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
	public Book book;
	
	@Column(name = "customer_id")
	public Integer customerId;
	
	@ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
	public Customer customer;
	
	public static BookCustomer create(Integer bookId, Integer customerId) {
		BookCustomer bookCustomer = new BookCustomer();
		bookCustomer.bookId = bookId;
		bookCustomer.customerId = customerId;
		
		return bookCustomer;
	}
	
	public static BookCustomer create(Integer bookId, Book book, Integer customerId, Customer customer) {
		BookCustomer bookCustomer = new BookCustomer();
		bookCustomer.bookId = bookId;
		bookCustomer.customerId = customerId;
		bookCustomer.book = book;
		bookCustomer.customer = customer; 
		
		return bookCustomer;
	}
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder().append(id).append(". ").append(bookId).append(" ").append(customerId);
		
		if(book != null) {
			description.append("\nBook: ").append(book);
		}
		
		if(customer != null) {
			description.append("\nCustomer: ").append(customer);
		}
		return description.toString();
	}
}
