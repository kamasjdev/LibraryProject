package mappings;

import java.util.List;
import java.util.Map;

import entities.BookCustomer;
import exceptions.mappings.bookauthor.CannotFindBookAuthorFieldException;
import interfaces.MapEntity;

public class BookCustomerMapping implements MapEntity<BookCustomer> {

	@Override
	public BookCustomer Map(List<Map<String, Object>> fields) {
		BookCustomer bookCustomer = null;
		
		if(fields == null) {
			return bookCustomer;
		}
		
		if(fields.isEmpty()) {
			return bookCustomer;
		}	
		
		bookCustomer = new BookCustomer();
		
		Object id = fields.stream().filter(f -> f.containsKey("bookcustomer.Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bookcustomer.Id");
		}).get("bookcustomer.Id");
		
		bookCustomer.id = (Integer) id;
		
		Object customerId = fields.stream().filter(f -> f.containsKey("bookcustomer.Customer_Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bookcustomer.Customer_Id");
		}).get("bookcustomer.Customer_Id");
		
		bookCustomer.customerId = (Integer) customerId;
		
		Object bookId = fields.stream().filter(f -> f.containsKey("bookcustomer.Book_Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bookcustomer.Book_Id");
		}).get("bookcustomer.Book_Id");
		
		bookCustomer.bookId = (Integer) bookId;
		
		if(bookCustomer.id == null) {
			return null;
		}
		
		return bookCustomer;
	}
}
