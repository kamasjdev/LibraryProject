package dto;

import java.util.Set;

public class CustomerDto extends BaseDto {
	public String firstName;
	public String lastName;
	public Integer limit;
	public boolean canBorrow;
	
	public Set<BookCustomerDto> books;
	public Set<BillDto> bills;
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder().append(id).append(". ").append(firstName)
				.append(" ").append(lastName).append(" ").append(limit).append(" ").append(canBorrow);
		
		if(!books.isEmpty()) {
			description.append("\nBooks borrowed by customer:\n");
			
			for(BookCustomerDto bookCustomer : books) {
				description.append(bookCustomer);
				description.append("\n");
			}
		}
		
		if(!bills.isEmpty()) {
			description.append("\nCustomer bills:\n");
			
			for(BillDto bill : bills) {
				description.append(bill);
				description.append("\n");
			}
		}
		
		return description.toString();
	}
}
