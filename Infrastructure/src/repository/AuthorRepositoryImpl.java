package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import exceptions.repository.author.AuthorCannotBeNullException;
import exceptions.repository.author.AuthorIdCannotBeNullException;
import interfaces.AuthorRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class AuthorRepositoryImpl extends BaseRepository implements AuthorRepository {
	private DbClient dbClient;
	private MapEntity<Author> mapper;
	private MapEntity<BookAuthor> bookAuthorMapper;
	private MapEntity<Book> bookMapper;
	
	public AuthorRepositoryImpl(DbClient dbClient, MapEntity<Author> mapper, MapEntity<BookAuthor> bookAuthorMapper, MapEntity<Book> bookMapper) {
		this.dbClient = dbClient;
		this.mapper = mapper;
		this.bookAuthorMapper = bookAuthorMapper;
		this.bookMapper = bookMapper;
	}
	
	@Override
	public Integer add(Author entity) {
		if(entity == null) {
			throw new AuthorCannotBeNullException();
		}
		
		Integer id = dbClient.insert("INSERT INTO AUTHORS(FIRST_NAME,LAST_NAME) VALUES(?, ?)", entity.person.firstName, entity.person.lastName);
		return id;
	}

	@Override
	public void delete(Author entity) {
		if(entity == null) {
			throw new AuthorCannotBeNullException();
		}
		
		delete(entity.id);
	}
	

	@Override
	public void delete(Integer id) {
		if(id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		dbClient.delete("DELETE FROM AUTHORS WHERE id = ?", id);
	}

	@Override
	public void update(Author entity) {
		if(entity.id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		dbClient.update("UPDATE AUTHORS SET FIRST_NAME = ?, LAST_NAME = ? WHERE id = ?", entity.person.firstName, entity.person.lastName, entity.id);
	}

	@Override
	public Author get(Integer id) {
		if(id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		List<List<Map<String, Object>>> authors = dbClient.executeQuery("SELECT * FROM AUTHORS WHERE id = ?", id);
		Author author = null;
		
		if(authors.size() > 0) {
			List<Map<String, Object>> authorsFields = authors.get(0);
			author = mapper.Map(authorsFields);
		}
		
		return author;
	}

	@Override
	public List<Author> getAll() {
		List<Author> authors = new ArrayList<Author>();
		List<List<Map<String, Object>>> authorsFromDb = dbClient.executeQuery("SELECT * FROM AUTHORS");

		for(List<Map<String,Object>> author : authorsFromDb) {
			authors.add(mapper.Map(author));
		}
		
		return authors;
	}

	@Override
	public Author getAuthorDetails(Integer id) {
		if(id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		List<List<Map<String, Object>>> authors = dbClient.executeQuery(
				"SELECT * FROM AUTHORS a "
				+ "LEFT JOIN BOOKAUTHOR ba ON a.id = ba.author_id "
				+ "LEFT JOIN BOOKS b ON b.id = ba.book_id "
				+ "WHERE a.id = ?", id);
		Author author = null;
		
		if(authors.size() == 0) {
			return author;
		}
		
		List<Map<String, Object>> authorsFields = authors.get(0);
		author = mapper.Map(authorsFields);
		List<List<Map<String,Object>>> bookAuthors = getConnectedEntities("bookauthor", authors);
		
		if(bookAuthors.isEmpty()) {
			return author;
		}
			
		for(List<Map<String, Object>> fields : bookAuthors) {
			BookAuthor bookAuthor = bookAuthorMapper.Map(fields);
			if(bookAuthor != null) {
				author.books.add(bookAuthor);
			}
		}
		
		List<List<Map<String, Object>>> books = getConnectedEntities("books", authors);
		
		for(List<Map<String, Object>> fields : books) {
			Book book = bookMapper.Map(fields);
			BookAuthor bookAuthor = author.books.stream().filter(b -> b.bookId.equals(book.id)).findFirst().orElse(null);
			if(bookAuthor != null) {
				bookAuthor.book = book;
			}
		}
		
		return author;
	}
	
}
