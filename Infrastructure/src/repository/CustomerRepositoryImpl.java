package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Book;
import entities.BookCustomer;
import entities.Customer;
import exceptions.repository.customer.CustomerCannotBeNullException;
import exceptions.repository.customer.CustomerIdCannotBeNullException;
import interfaces.CustomerRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class CustomerRepositoryImpl extends BaseRepository implements CustomerRepository {
	private DbClient dbClient;
	private MapEntity<Customer> mapper;
	private MapEntity<BookCustomer> bookCustomerMapper;
	private MapEntity<Book> bookMapper;
	
	public CustomerRepositoryImpl(DbClient dbClient, MapEntity<Customer> mapper, MapEntity<BookCustomer> bookCustomerMapper, MapEntity<Book> bookMapper) {
		this.dbClient = dbClient;
		this.mapper = mapper;
		this.bookCustomerMapper = bookCustomerMapper;
		this.bookMapper = bookMapper;
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
}
