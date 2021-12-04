package mappings;

import java.math.BigDecimal;
import java.util.List;

import entities.Bill;
import entities.BookAuthor;
import exceptions.mappings.bookauthor.CannotFindBookAuthorFieldException;
import interfaces.MapEntity;

public class BillMapping implements MapEntity<Bill> {

	@Override
	public Bill Map(List<java.util.Map<String, Object>> fields) {
		Bill bill = null;
		
		if(fields == null) {
			return bill;
		}
		
		if(fields.isEmpty()) {
			return bill;
		}	
		
		bill = new Bill();
		
		Object id = fields.stream().filter(f -> f.containsKey("bills.Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bills.Id");
		}).get("bills.Id");
		
		bill.id = (Integer) id;
		
		Object cost = fields.stream().filter(f -> f.containsKey("bills.cost")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bills.cost");
		}).get("bills.cost");
		
		bill.cost = (BigDecimal) cost;
		
		Object customerId = fields.stream().filter(f -> f.containsKey("bills.customer_id")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bills.customer_id");
		}).get("bills.customer_id");
		
		bill.customerId = (Integer) customerId;
				
		if(bill.id == null) {
			return null;
		}
		
		return bill;
	}
}
