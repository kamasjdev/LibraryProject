package entities;

import common.BaseEntity;

public class BookCustomer extends BaseEntity {
	public Integer bookId;
	public Book book;
	public Integer customerId;
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
