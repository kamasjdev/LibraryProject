package mappings;

import java.util.List;
import java.util.Map;

import entities.Customer;
import exceptions.mappings.author.CannotFindAuthorFieldException;
import interfaces.MapEntity;

public class CustomerMapping implements MapEntity<Customer> {

	@Override
	public Customer Map(List<Map<String, Object>> fields) {
		Customer customer = null;
		
		if(fields == null) {
			return customer;
		}
		
		if(fields.isEmpty()) {
			return customer;
		}	
		
		customer = new Customer();
		
		Object id = fields.stream().filter(f -> f.containsKey("customers.Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("customers.Id");
		}).get("customers.Id");
		
		customer.id = (Integer) id;
		
		Object firstName = fields.stream().filter(f -> f.containsKey("customers.FIRST_NAME")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("customers.FIRST_NAME");
		}).get("customers.FIRST_NAME");
		
		customer.person.firstName = (String) firstName;
		
		Object lastName = fields.stream().filter(f -> f.containsKey("customers.LAST_NAME")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("customers.LAST_NAME");
		}).get("customers.LAST_NAME");
		
		customer.person.lastName = (String) lastName;
		
		Object limit = fields.stream().filter(f -> f.containsKey("customers.books_limit")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("customers.books_limit");
		}).get("customers.books_limit");
		
		customer.limit = (Integer) limit;
		
		Object canBorrow = fields.stream().filter(f -> f.containsKey("customers.can_borrow")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("customers.can_borrow");
		}).get("customers.can_borrow");
		
		customer.canBorrow = (Boolean) canBorrow;
		
		if(customer.id == null) {
			return null;
		}
		
		return customer;
	}

}
