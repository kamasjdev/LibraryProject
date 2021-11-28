package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import entities.Customer;
import exceptions.repository.bookauthor.AuthorsCannotBeEmptyOrNullException;
import exceptions.repository.bookauthor.BookCannotBeNullException;
import exceptions.repository.bookauthor.BookIdCannotBeNullException;
import interfaces.BookRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class BookRepositoryImpl implements BookRepository {
	private DbClient dbClient;
	private MapEntity<Book> mapper;
	private MapEntity<BookAuthor> bookAuthorMapper;
	private MapEntity<Author> authorMapper;
	private MapEntity<BookCustomer> bookCustomerMapper;
	private MapEntity<Customer> customerMapper;
	
	public BookRepositoryImpl(DbClient dbClient, MapEntity<Book> mapper, MapEntity<BookAuthor> bookAuthorMapper, MapEntity<Author> authorMapper, MapEntity<BookCustomer> bookCustomerMapper, MapEntity<Customer> customerMapper) {
		this.dbClient = dbClient;
		this.mapper = mapper;
		this.bookAuthorMapper = bookAuthorMapper;
		this.authorMapper = authorMapper;
		this.bookCustomerMapper = bookCustomerMapper;
		this.customerMapper = customerMapper;
	}
	
	@Override
	public Integer add(Book entity) {
		validateBook(entity);
		
		Integer id = dbClient.insert("INSERT INTO BOOKS(Book_name, ISBN, cost) VALUES(?, ?, ?)", entity.bookName, entity.ISBN, entity.bookCost);
		
		for(BookAuthor bookAuthor : entity.authors) {
			dbClient.insert("INSERT INTO BOOKAUTHOR (Book_Id, Author_Id) VALUES(?, ?)", id, bookAuthor.authorId);
		}
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		if(id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		dbClient.delete("DELETE FROM BOOKAUTHOR WHERE Book_Id = ?", id);
		dbClient.delete("DELETE FROM BOOKS WHERE id = ?", id);
	}

	@Override
	public void delete(Book entity) {
		if(entity == null) {
			throw new BookCannotBeNullException();
		}
		
		delete(entity.id);
	}

	@Override
	public void update(Book entity) {
		if(entity.id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		validateBook(entity);
		dbClient.update("UPDATE BOOKS SET Book_name = ?, ISBN = ?, cost = ?, borrowed = ? WHERE id = ?", entity.bookName, entity.ISBN, entity.bookCost, entity.borrowed, entity.id);
		dbClient.delete("DELETE FROM BOOKAUTHOR WHERE Book_Id = ?", entity.id);
		
		for(BookAuthor bookAuthor : entity.authors) {
			dbClient.insert("INSERT INTO BOOKAUTHOR (Book_Id, Author_Id) VALUES(?, ?)", entity.id, bookAuthor.authorId);
		}
	}
	
	@Override
	public Book get(Integer id) {
		if(id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		Book book = null;
		
		List<List<Map<String, Object>>> books = dbClient.executeQuery(
				"SELECT * FROM BOOKS b " +
				"JOIN BOOKAUTHOR ba ON ba.Book_Id = b.Id " +
				"JOIN AUTHORS a ON a.Id = ba.Author_Id " + 
				"WHERE b.id = ?", id);
		
		if(books.size() == 0) {
			return book;
		}
		
		List<Map<String, Object>> booksFields = books.get(0);
		book = mapper.Map(booksFields);
		List<List<Map<String,Object>>> bookAuthors = getConnectedEntities("bookauthor", books);
		
		for(List<Map<String, Object>> fields : bookAuthors) {
			BookAuthor bookAuthor = bookAuthorMapper.Map(fields);
			if(bookAuthor != null) {
				book.authors.add(bookAuthor);
			}
		}
		
		List<List<Map<String, Object>>> authors = getConnectedEntities("authors", books);
		
		for(List<Map<String, Object>> fields : authors) {
			Author author = authorMapper.Map(fields);
			BookAuthor bookAuthor = book.authors.stream().filter(b -> b.authorId.equals(author.id)).findFirst().orElse(null);
			if(bookAuthor != null) {
				bookAuthor.author = author;
			}
		}
		
		return book;
	}

	@Override
	public List<Book> getAll() {
		List<Book> books = new ArrayList<Book>();
		List<List<Map<String, Object>>> booksFromDb = dbClient.executeQuery(
				"SELECT * FROM BOOKS b " +
				"JOIN BOOKAUTHOR ba ON ba.Book_Id = b.Id " +
				"JOIN AUTHORS a ON a.Id = ba.Author_Id ");

		List<List<Map<String, Object>>> booksDistincted = new ArrayList<List<Map<String,Object>>>();
		HashSet<Map<String, Object>> bookFieldsUsed = new HashSet<Map<String,Object>>();
		
		for(List<Map<String, Object>> bookFields : booksFromDb) {
			Map<String, Object> id = bookFields.stream().filter(f -> f.containsKey("books.Id")).findFirst().orElse(null);
			Map<String, Object> idBefore = bookFieldsUsed.stream()
					.filter(f -> f.get("books.Id") == id.get("books.Id"))
					.findFirst().orElse(null);
			
			if(id != null && !id.equals(idBefore)) {
				booksDistincted.add(bookFields);
			}
			
			bookFieldsUsed.add(id);
		}
		
		for(List<Map<String,Object>> bookFromDb : booksDistincted) {
			Book book = mapper.Map(bookFromDb);
			books.add(book);
		}
		
		List<List<Map<String, Object>>> authors = getConnectedEntities("authors", booksFromDb);
		
		List<List<Map<String, Object>>> authorsDistincted = new ArrayList<List<Map<String,Object>>>();
		HashSet<Map<String, Object>> authorFieldsUsed = new HashSet<Map<String,Object>>();
		
		for(List<Map<String, Object>> authorFields : authors) {
			Map<String, Object> id = authorFields.stream().filter(f -> f.containsKey("authors.Id")).findFirst().orElse(null);
			Map<String, Object> idBefore = authorFieldsUsed.stream()
					.filter(f -> f.get("authors.Id") == id.get("authors.Id"))
					.findFirst().orElse(null);
			
			if(id != null && !id.equals(idBefore)) {
				authorsDistincted.add(authorFields);
			}
			
			authorFieldsUsed.add(id);
		}
		
		List<Author> authorsUsed = new ArrayList<Author>();
		
		for(List<Map<String, Object>> fields : authorsDistincted) {
			Author author = authorMapper.Map(fields);
			authorsUsed.add(author);
		}
		
		List<List<Map<String,Object>>> bookAuthors = getConnectedEntities("bookauthor", booksFromDb);
			
		for(List<Map<String, Object>> fields : bookAuthors) {
			BookAuthor bookAuthor = bookAuthorMapper.Map(fields);
			
			if(bookAuthor != null) {
				Author author = authorsUsed.stream().filter(a -> a.id == bookAuthor.authorId).findFirst().orElse(null);
				
				if(author != null) {
					bookAuthor.author = author;
				}
				
				Book book = books.stream().filter(b -> b.id == bookAuthor.bookId).findFirst().orElse(null);
				
				if(book != null) {
					book.authors.add(bookAuthor);
				}
			}
		}
		
		return books;
	}

	@Override
	public Book getBookDetails(Integer id) {
		if(id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		Book book = null;
		
		List<List<Map<String, Object>>> books = dbClient.executeQuery(
				"SELECT * FROM BOOKS b " +
				"JOIN BOOKAUTHOR ba ON ba.Book_Id = b.Id " +
				"JOIN AUTHORS a ON a.Id = ba.Author_Id " + 
				"LEFT JOIN BOOKCUSTOMER bc ON bc.Book_Id = b.Id " +
				"LEFT JOIN CUSTOMERS c ON bc.Customer_Id = c.Id " +
				"WHERE b.id = ?", id);
	
		if(books.isEmpty()) {
			return book;
		}
		
		List<Map<String, Object>> booksFields = books.get(0);
		book = mapper.Map(booksFields);
		List<List<Map<String,Object>>> bookAuthors = getConnectedEntities("bookauthor", books);
		
		for(List<Map<String, Object>> fields : bookAuthors) {
			BookAuthor bookAuthor = bookAuthorMapper.Map(fields);
			if(bookAuthor != null) {
				book.authors.add(bookAuthor);
			}
		}
		
		List<List<Map<String, Object>>> authors = getConnectedEntities("authors", books);
		
		for(List<Map<String, Object>> fields : authors) {
			Author author = authorMapper.Map(fields);
			BookAuthor bookAuthor = book.authors.stream().filter(b -> b.authorId.equals(author.id)).findFirst().orElse(null);
			if(bookAuthor != null) {
				bookAuthor.author = author;
			}
		}
		
		List<List<Map<String, Object>>> bookCustomers = getConnectedEntities("bookcustomer", books);
		
		if(bookCustomers.isEmpty()) {
			return book;
		}
			
		for(List<Map<String, Object>> fields : bookCustomers) {
			BookCustomer bookCustomer = bookCustomerMapper.Map(fields);
			if(bookCustomer != null) {
				book.customers.add(bookCustomer);
			}
		}
		
		List<List<Map<String, Object>>> customers = getConnectedEntities("customers", books);
		
		for(List<Map<String, Object>> fields : customers) {
			Customer customer = customerMapper.Map(fields);
			BookCustomer bookCustomer = book.customers.stream().filter(c -> c.customerId.equals(customer.id)).findFirst().orElse(null);
			if(bookCustomer != null) {
				bookCustomer.customer = customer;
			}
		}
		
		return book;
	}
	
	private void validateBook(Book book) {
		if(book.authors == null) {
			throw new AuthorsCannotBeEmptyOrNullException();
		}
		
		if(book.authors.isEmpty()) {
			throw new AuthorsCannotBeEmptyOrNullException();
		}
	}
	
	private List<List<Map<String,Object>>> getConnectedEntities(String entityTableName, List<List<Map<String, Object>>> dataFromDb) {
		List<List<Map<String,Object>>> entities = new ArrayList<List<Map<String,Object>>>();
		
		for(List<Map<String, Object>> fields : dataFromDb) {
			List<Map<String, Object>> entity = new ArrayList<Map<String,Object>>();
			
			for(Map<String, Object> field : fields) {
				String entityField = field.keySet().stream().filter(f-> f.contains(entityTableName)).findFirst().orElse(null);
				
				if(entityField != null) {
					Object fieldValue = field.get(entityField);
					Map<String, Object> entityFieldNameAndValue = new HashMap<String, Object>();
					entityFieldNameAndValue.put(entityField, fieldValue);
					entity.add(entityFieldNameAndValue);
				}
			}
			
			entities.add(entity);			
		}
		
		return entities;
	}
}
