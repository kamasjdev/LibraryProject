package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import exceptions.repository.bookauthor.BookAuthorCannotBeNullException;
import exceptions.repository.bookauthor.BookAuthorIdCannotBeNullException;
import interfaces.BookAuthorRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class BookAuthorRepositoryImpl extends BaseRepository implements BookAuthorRepository {
	private final DbClient dbClient;
	private final MapEntity<BookAuthor> mapper;
	private final MapEntity<Book> bookMapper;
	private final MapEntity<Author> authorMapper;
	
	public BookAuthorRepositoryImpl(DbClient dbClient, MapEntity<BookAuthor> mapper, MapEntity<Book> bookMapper, MapEntity<Author> authroMapper) {
		this.dbClient = dbClient;
		this.mapper = mapper;
		this.bookMapper = bookMapper;
		this.authorMapper = authroMapper;
	}
	
	@Override
	public Integer add(BookAuthor entity) {
		if(entity == null) {
			throw new BookAuthorCannotBeNullException();
		}
		
		Integer id = dbClient.insert("INSERT INTO BOOKAUTHOR(Book_Id ,  Author_Id) VALUES(?, ?)", entity.bookId, entity.authorId);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		if(id == null) {
			throw new BookAuthorIdCannotBeNullException();
		}
		
		dbClient.delete("DELETE FROM BOOKAUTHOR WHERE id = ?", id);
	}

	@Override
	public void delete(BookAuthor entity) {
		if(entity == null) {
			throw new BookAuthorCannotBeNullException();
		}
		
		delete(entity.id);
	}

	@Override
	public void update(BookAuthor entity) {
		if(entity == null) {
			throw new BookAuthorCannotBeNullException();
		}
		
		if(entity.id == null) {
			throw new BookAuthorIdCannotBeNullException();
		}
		
		dbClient.update("UPDATE BOOKAUTHOR SET Book_Id = ?, Author_Id = ? WHERE id = ?", entity.bookId, entity.authorId, entity.id);
	}

	@Override
	public BookAuthor get(Integer id) {
		if(id == null) {
			throw new BookAuthorIdCannotBeNullException();
		}
		
		List<List<Map<String, Object>>> bookAuthors = dbClient.executeQuery(
				"SELECT * FROM BOOKAUTHOR ba " +
				"JOIN BOOKS b ON b.id = ba.Book_Id " +
				"JOIN AUTHORS a ON a.id = ba.Author_Id " +
				"WHERE ba.id = ?", 
				id);
		BookAuthor bookAuthor = null;
		
		if(bookAuthors.size() > 0) {
			List<Map<String, Object>> bookAuthorsFields = bookAuthors.get(0);
			bookAuthor = mapToBookAuthor(bookAuthorsFields);
		}
		
		return bookAuthor;
	}

	@Override
	public List<BookAuthor> getAll() {
		List<List<Map<String, Object>>> bookAuthorsFields = dbClient.executeQuery(
				"SELECT * FROM BOOKAUTHOR ba " +
				"JOIN BOOKS b ON b.id = ba.Book_Id " +
				"JOIN AUTHORS a ON a.id = ba.Author_Id ");
		List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
		
		for(List<Map<String, Object>> fields : bookAuthorsFields) {
			BookAuthor bookAuthor = mapToBookAuthor(fields);
			bookAuthors.add(bookAuthor);
		}
		
		return bookAuthors;
	}

	@Override
	public List<BookAuthor> getByAuthorId(Integer authorId) {
		List<List<Map<String, Object>>> bookAuthorsFields = dbClient.executeQuery(
				"SELECT * FROM BOOKAUTHOR ba " +
				"JOIN BOOKS b ON b.id = ba.Book_Id " +
				"JOIN AUTHORS a ON a.id = ba.Author_Id " +
				"WHERE a.id = ? ", authorId);
		List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();

		for(List<Map<String, Object>> fields : bookAuthorsFields) {
			BookAuthor bookAuthor = mapToBookAuthor(fields);
			bookAuthors.add(bookAuthor);
		}
		
		return bookAuthors;
	}

	@Override
	public List<BookAuthor> getByBookId(Integer bookId) {
		List<List<Map<String, Object>>> bookAuthorsFields = dbClient.executeQuery(
				"SELECT * FROM BOOKAUTHOR ba " +
				"JOIN BOOKS b ON b.id = ba.Book_Id " +
				"JOIN AUTHORS a ON a.id = ba.Author_Id " +
				"WHERE b.id = ? ", bookId);
		List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();

		for(List<Map<String, Object>> fields : bookAuthorsFields) {
			BookAuthor bookAuthor = mapToBookAuthor(fields);			
			bookAuthors.add(bookAuthor);
		}
		
		return bookAuthors;
	}
	
	private BookAuthor mapToBookAuthor(List<Map<String, Object>> fields) {
		BookAuthor bookAuthor = mapper.Map(fields);
		
		List<Map<String, Object>> bookFields = getConnectedEntity("books", fields);
		Book book = bookMapper.Map(bookFields);
		bookAuthor.book = book;
		
		List<Map<String, Object>> authorFields = getConnectedEntity("authors", fields);
		Author author = authorMapper.Map(authorFields);
		bookAuthor.author = author;
		
		return bookAuthor;
	}
}
