package dto;

public class BookCustomerDto extends BaseDto {
	public Integer bookId;
	public BookDto book;
	public Integer customerId;
	public CustomerDto customer;
	
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
