package dto;

import java.math.BigDecimal;
import java.util.Set;

public class BookDto extends BaseDto {
	public String bookName;
	public String ISBN;
	public BigDecimal bookCost;
	public boolean borrowed;
	
	public Set<BookAuthorDto> authors;
	public Set<BookCustomerDto> customers;
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder().append(id).append(". ").append(bookName)
				.append(" ").append(bookCost).append(" ").append(borrowed);
		
		if(!authors.isEmpty()) {
			description.append("\nBook authors:\n");
			
			for(BookAuthorDto bookAuthor : authors) {
				description.append(bookAuthor);
				description.append("\n");
			}
		}
		
		if(!customers.isEmpty()) {
			description.append("\nBorrowed by customers:\n");
			
			for(BookCustomerDto bookCustomer : customers) {
				description.append(bookCustomer);
				description.append("\n");
			}
		}
		
		return description.toString();
	}
}
