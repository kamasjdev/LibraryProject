package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Book;
import entities.BookCustomer;
import entities.Customer;
import exceptions.repository.bookcustomer.BookCustomerCannotBeNullException;
import exceptions.repository.bookcustomer.BookCustomerIdCannotBeNullException;
import interfaces.BookCustomerRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class BookCustomerRepositoryImpl extends BaseRepository implements BookCustomerRepository {
	private final DbClient dbClient;
	private final MapEntity<BookCustomer> mapper;
	private final MapEntity<Book> bookMapper;
	private final MapEntity<Customer> customerMapper;
	
	public BookCustomerRepositoryImpl(DbClient dbClient, MapEntity<BookCustomer> mapper, MapEntity<Book> bookMapper, MapEntity<Customer> customerMapper) {
		this.dbClient = dbClient;
		this.mapper = mapper;
		this.bookMapper = bookMapper;
		this.customerMapper = customerMapper;
	}
	
	@Override
	public Integer add(BookCustomer entity) {
		if(entity == null) {
			throw new BookCustomerCannotBeNullException();
		}
		
		Integer id = dbClient.insert("INSERT INTO BOOKCUSTOMER(Book_Id,  Customer_Id) VALUES(?, ?)", entity.bookId, entity.customerId);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		if(id == null) {
			throw new BookCustomerIdCannotBeNullException();
		}
		
		dbClient.delete("DELETE FROM BOOKCUSTOMER WHERE id = ?", id);
		
	}

	@Override
	public void delete(BookCustomer entity) {
		if(entity == null) {
			throw new BookCustomerCannotBeNullException();
		}

		delete(entity.id);
	}

	@Override
	public void update(BookCustomer entity) {
		if(entity == null) {
			throw new BookCustomerCannotBeNullException();
		}
		
		if(entity.id == null) {
			throw new BookCustomerIdCannotBeNullException();
		}
		
		dbClient.update("UPDATE BOOKCUSTOMER SET Book_Id = ?, Customer_Id = ? WHERE id = ?", entity.bookId, entity.customerId, entity.id);
	}

	@Override
	public BookCustomer get(Integer id) {
		if(id == null) {
			throw new BookCustomerIdCannotBeNullException();
		}
		
		List<List<Map<String, Object>>> bookCustomers = dbClient.executeQuery(
				"SELECT * FROM BOOKCUSTOMER bc " +
				"JOIN BOOKS b ON b.id = bc.Book_Id " +
				"JOIN CUSTOMERS c ON c.id = bc.Customer_Id " +
				"WHERE bc.id = ?", 
				id);
		BookCustomer bookCustomer = null;
		
		if(bookCustomers.size() > 0) {
			List<Map<String, Object>> bookCustomersFields = bookCustomers.get(0);
			bookCustomer = mapToBookCustomer(bookCustomersFields);
		}
		
		return bookCustomer;
	}

	@Override
	public List<BookCustomer> getAll() {
		List<List<Map<String, Object>>> bookAuthorsFields = dbClient.executeQuery(
				"SELECT * FROM BOOKCUSTOMER bc " +
				"JOIN BOOKS b ON b.id = bc.Book_Id " +
				"JOIN CUSTOMERS c ON c.id = bc.Customer_Id ");
		List<BookCustomer> bookCustomers = new ArrayList<BookCustomer>();
		
		for(List<Map<String, Object>> fields : bookAuthorsFields) {
			BookCustomer bookCustomer = mapToBookCustomer(fields);			
			bookCustomers.add(bookCustomer);
		}
		
		return bookCustomers;
	}

	@Override
	public BookCustomer getBookCustomerByBookIdAndCustomerId(Integer bookId, Integer customerId) {
		List<List<Map<String, Object>>> bookCustomers = dbClient.executeQuery(
				"SELECT * FROM BOOKCUSTOMER bc " +
				"JOIN BOOKS b ON b.id = bc.Book_Id " +
				"JOIN CUSTOMERS c ON c.id = bc.Customer_Id " +
				"WHERE b.id = ? AND c.id = ? ", 
				bookId, customerId);
		BookCustomer bookCustomer = null;
		
		if(bookCustomers.size() > 0) {
			List<Map<String, Object>> bookCustomersFields = bookCustomers.get(0);
			bookCustomer = mapToBookCustomer(bookCustomersFields);
		}
		
		return bookCustomer;
	}
	
	private BookCustomer mapToBookCustomer(List<Map<String, Object>> fields) {
		BookCustomer bookCustomer = mapper.Map(fields);
		
		List<Map<String, Object>> bookFields = getConnectedEntity("books", fields);
		Book book = bookMapper.Map(bookFields);
		bookCustomer.book = book;
		
		List<Map<String, Object>> authorFields = getConnectedEntity("customers", fields);
		Customer customer = customerMapper.Map(authorFields);
		bookCustomer.customer = customer;
		
		return bookCustomer;
	}

}
