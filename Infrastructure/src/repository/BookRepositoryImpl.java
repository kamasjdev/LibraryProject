package repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import entities.Customer;
import exceptions.repository.book.BookCannotBeNullException;
import exceptions.repository.book.BookIdCannotBeNullException;
import exceptions.repository.bookauthor.BookAuthorsCannotBeEmptyOrNullException;
import interfaces.BookRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class BookRepositoryImpl extends BaseRepository implements BookRepository {
	private final DbClient dbClient;
	private final MapEntity<Book> mapper;
	private final MapEntity<BookAuthor> bookAuthorMapper;
	private final MapEntity<Author> authorMapper;
	private final MapEntity<BookCustomer> bookCustomerMapper;
	private final MapEntity<Customer> customerMapper;
	
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
		validateBook(entity);
		
		if(entity.id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		dbClient.update("UPDATE BOOKS SET Book_name = ?, ISBN = ?, cost = ?, borrowed = ? WHERE id = ?", entity.bookName, entity.ISBN, entity.bookCost, entity.borrowed, entity.id);
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

		HashSet<Map<String, Object>> bookFieldsUsed = new HashSet<Map<String,Object>>();
		
		for(List<Map<String, Object>> bookFields : booksFromDb) {
			Map<String, Object> id = bookFields.stream().filter(f -> f.containsKey("books.Id")).findFirst().orElse(null);
			Map<String, Object> idBefore = bookFieldsUsed.stream()
					.filter(f -> f.get("books.Id") == id.get("books.Id"))
					.findFirst().orElse(null);
			
			if(id != null && !id.equals(idBefore)) {
				Book book = mapper.Map(bookFields);
				books.add(book);
			}
			
			BookAuthor bookAuthor = mapRowsToBookAuthor(bookFields);
			Integer bookId = (Integer) id.get("books.Id");
			Optional<Book> optionalBook = books.stream().filter(b -> b.id == bookId).findFirst();
			
			if(optionalBook.isPresent()) {
				Book book = optionalBook.get();
				book.authors.add(bookAuthor);
			}
			
			bookFieldsUsed.add(id);
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
		
		for(List<Map<String, Object>> bookFields : books) {
			BookAuthor bookAuthor = mapRowsToBookAuthor(bookFields);
			book.authors.add(bookAuthor);
			
			BookCustomer bookCustomer = mapRowsToBookCustomer(bookFields);
			if(bookCustomer != null) {
				book.customers.add(bookCustomer);
			}
		}
		
		return book;
	}
	
	private void validateBook(Book book) {
		if(book == null) {
			throw new BookCannotBeNullException();
		}
		
		if(book.authors == null) {
			throw new BookAuthorsCannotBeEmptyOrNullException();
		}
		
		if(book.authors.isEmpty()) {
			throw new BookAuthorsCannotBeEmptyOrNullException();
		}
	}
	
	private BookAuthor mapRowsToBookAuthor(List<Map<String,Object>> fields) {
		List<Map<String, Object>> bookAuthorFields = getConnectedEntity("bookauthor", fields);
		BookAuthor bookAuthor = bookAuthorMapper.Map(bookAuthorFields);
		
		// zabezpieczenie przed null
		if(bookAuthor == null) {
			return bookAuthor;
		}
		
		List<Map<String, Object>> authorFields = getConnectedEntity("authors", fields);
		Author author = authorMapper.Map(authorFields);
		bookAuthor.author = author;
		
		return bookAuthor;
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
		
		List<Map<String, Object>> customerFields = getConnectedEntity("customers", fields);
		Customer customer = customerMapper.Map(customerFields);
		bookCustomer.customer = customer;
		
		return bookCustomer;
	}

	@Override
	public int getCount() {
		List<List<Map<String, Object>>> countQuery = dbClient.executeQuery(
				"SELECT COUNT(b.id) FROM BOOKS b ");
		List<Map<String, Object>> singleResult = countQuery.get(0);
		int count = ((Long) singleResult.get(0).get("null.COUNT(b.id)")).intValue();
		return count;
	}

	@Override
	public Book getBookWithoutAuthors(Integer id) {
		if(id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		Book book = null;
		
		List<List<Map<String, Object>>> books = dbClient.executeQuery(
				"SELECT * FROM BOOKS b " +
				"WHERE b.id = ?", id);
		
		if(books.size() == 0) {
			return book;
		}
		
		List<Map<String, Object>> booksFields = books.get(0);
		book = mapper.Map(booksFields);
		
		return book;
	}
}
