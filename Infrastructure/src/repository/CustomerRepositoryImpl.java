package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Bill;
import entities.Book;
import entities.BookCustomer;
import entities.Customer;
import exceptions.repository.customer.CustomerCannotBeNullException;
import exceptions.repository.customer.CustomerIdCannotBeNullException;
import interfaces.CustomerRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class CustomerRepositoryImpl extends BaseRepository implements CustomerRepository {
	private final DbClient dbClient;
	private final MapEntity<Customer> mapper;
	private final MapEntity<BookCustomer> bookCustomerMapper;
	private final MapEntity<Book> bookMapper;
	private final MapEntity<Bill> billMapper;
	
	public CustomerRepositoryImpl(DbClient dbClient, MapEntity<Customer> mapper, MapEntity<BookCustomer> bookCustomerMapper, MapEntity<Book> bookMapper, MapEntity<Bill> billMapper) {
		this.dbClient = dbClient;
		this.mapper = mapper;
		this.bookCustomerMapper = bookCustomerMapper;
		this.bookMapper = bookMapper;
		this.billMapper = billMapper;
	}
	
	@Override
	public Integer add(Customer entity) {
		if(entity == null) {
			throw new CustomerCannotBeNullException();
		}
		
		Integer id = dbClient.insert("INSERT INTO CUSTOMERS(FIRST_NAME, LAST_NAME, can_borrow, books_limit) VALUES(?, ?, ?, ?)", entity.person.firstName, entity.person.lastName, entity.canBorrow, entity.limit);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		if(id == null) {
			throw new CustomerIdCannotBeNullException();
		}
		
		dbClient.delete("DELETE FROM CUSTOMERS WHERE id = ?", id);
	}

	@Override
	public void delete(Customer entity) {
		if(entity == null) {
			throw new CustomerCannotBeNullException();
		}
		
		delete(entity.id);
	}

	@Override
	public void update(Customer entity) {
		if(entity == null) {
			throw new CustomerCannotBeNullException();
		}
		
		if(entity.id == null) {
			throw new CustomerIdCannotBeNullException();
		}
		
		dbClient.update("UPDATE CUSTOMERS SET FIRST_NAME = ?, LAST_NAME = ?, can_borrow = ?, books_limit = ? WHERE id = ?", entity.person.firstName, entity.person.lastName, entity.canBorrow, entity.limit, entity.id);
	}

	@Override
	public Customer get(Integer id) {
		if(id == null) {
			throw new CustomerIdCannotBeNullException();
		}
		
		List<List<Map<String, Object>>> customers = dbClient.executeQuery("SELECT * FROM CUSTOMERS WHERE id = ?", id);
		Customer customer = null;
		
		if(customers.size() > 0) {
			List<Map<String, Object>> customersFields = customers.get(0);
			customer = mapper.Map(customersFields);
		}
		
		return customer;
	}

	@Override
	public List<Customer> getAll() {
		List<Customer> customers = new ArrayList<Customer>();
		List<List<Map<String, Object>>> customersFromDb = dbClient.executeQuery("SELECT * FROM CUSTOMERS");

		for(List<Map<String,Object>> customer : customersFromDb) {
			customers.add(mapper.Map(customer));
		}
		
		return customers;
	}

	@Override
	public int getCount() {
		List<List<Map<String, Object>>> countQuery = dbClient.executeQuery("SELECT COUNT(c.id) FROM CUSTOMERS c");
		List<Map<String, Object>> singleResult = countQuery.get(0);
		int count = ((Long) singleResult.get(0).get("null.COUNT(c.id)")).intValue();
		return count;
	}

	@Override
	public Customer getDetails(Integer customerId) {
		Customer customer = null;
		List<List<Map<String, Object>>> customers = dbClient.executeQuery(
				"SELECT * FROM CUSTOMERS c " +
				"LEFT JOIN BOOKCUSTOMER bc ON bc.Customer_Id = c.id " +
				"LEFT JOIN BOOKS b ON b.id = bc.Book_Id " +
				"LEFT JOIN BILLS bil ON bil.customer_id = c.id " +
				"WHERE c.id = ?", customerId);
		
		if(customers.isEmpty()) {
			return customer;
		}
		
		List<Map<String, Object>> customersFields = customers.get(0);
		customer = mapper.Map(customersFields);
		
		for(List<Map<String, Object>> customerFields : customers) {			
			BookCustomer bookCustomer = mapRowsToBookCustomer(customerFields);
			
			if(bookCustomer != null) {
				customer.books.add(bookCustomer);
			}
			
			Bill bill = mapRowsToBill(customerFields);
			
			if(bill != null) {
				customer.bills.add(bill);
			}
		}
		
		return customer;
	}
	
	private Bill mapRowsToBill(List<Map<String, Object>> fields) {
		List<Map<String, Object>> billFields = getConnectedEntity("bills", fields);
		
		if(billFields.isEmpty()) {
			return null;
		}
		
		Bill bill = billMapper.Map(billFields);
		
		return bill;
	}

	private BookCustomer mapRowsToBookCustomer(List<Map<String,Object>> fields) {
		List<Map<String, Object>> bookCustomerFields = getConnectedEntity("bookcustomer", fields);
		
		if(bookCustomerFields.isEmpty()) {
			return null;
		}
		
		BookCustomer bookCustomer = bookCustomerMapper.Map(bookCustomerFields);
		if(bookCustomer == null) {
			return bookCustomer;
		}
		
		List<Map<String, Object>> customerFields = getConnectedEntity("books", fields);
		Book book = bookMapper.Map(customerFields);
		bookCustomer.book = book;
		
		return bookCustomer;
	}
}
