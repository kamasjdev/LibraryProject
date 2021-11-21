package entities;

import common.BaseEntity;

public class BookCustomer extends BaseEntity {
	public Integer bookId;
	public Integer customerId;
	
	public static BookCustomer create(Integer bookId, Integer customerId) {
		BookCustomer bookCustomer = new BookCustomer();
		bookCustomer.bookId = bookId;
		bookCustomer.customerId = customerId;
		
		return bookCustomer;
	}
	
	@Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s", id, bookId, customerId);
		return description;
	}
}
