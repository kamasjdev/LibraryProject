package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import entities.BookAuthor;
import interfaces.BookAuthorRepository;

public class BookAuthorRepositoryTests {
	private BookAuthorRepository bookAuthorRepository; 
	
	public BookAuthorRepositoryTests() {
		SessionManager.createSessionManager("hibernateTest.cfg.xml");
		bookAuthorRepository = new BookAuthorRepositoryImpl(SessionManager.getSessionFactory());
	}
	
	@Test
	public void given_valid_parameters_should_return_list_book_author() {
		List<BookAuthor> bookAuthors = bookAuthorRepository.getAll();
		
		assertThat(bookAuthors).isNotNull();
		assertThat(bookAuthors.size()).isGreaterThan(0);
	}
	
	@Test
	public void given_valid_id_when_get_book_author_should_return_book_author() {
		Integer id = 1;
		
		BookAuthor bookAuthor = bookAuthorRepository.get(id);
		
		assertThat(bookAuthor).isNotNull();
	}
	
	@Test
	public void given_valid_parameters_should_add_book_author() {
		Integer bookId = 7;
		Integer authorId = 1;
		BookAuthor bookAuthor = BookAuthor.create(bookId, authorId);
		
		Integer id = bookAuthorRepository.add(bookAuthor);
		
		assertThat(id).isNotNull();
	}
	
	@Test
	public void given_valid_parameters_should_update_book_author() {
		Integer id = 11;
		Integer authorId = 9;
		BookAuthor bookAuthor = bookAuthorRepository.get(id);
		bookAuthor.authorId = authorId;
		
		bookAuthorRepository.update(bookAuthor);
		
		BookAuthor bookAuthorUpdated = bookAuthorRepository.get(id);
		assertThat(bookAuthor).isNotNull();
		assertThat(bookAuthorUpdated.authorId).isEqualTo(authorId);
	}

	@Test
	public void given_valid_book_id_should_return_book_authors_by_book_id() {
		Integer bookId = 1;
		
		List<BookAuthor> bookAuthors = bookAuthorRepository.getByBookId(bookId);
		
		assertThat(bookAuthors.size()).isGreaterThan(0);
	}
	
	@Test
	public void given_valid_author_id_should_return_book_authors_by_author_id() {
		Integer authorId = 1;
		
		List<BookAuthor> bookAuthors = bookAuthorRepository.getByAuthorId(authorId);
		
		assertThat(bookAuthors.size()).isGreaterThan(0);
	}
	
	@Test
	public void given_valid_paramters_should_delete_book_author() {
		Integer id = 11;
		
		bookAuthorRepository.delete(id);
		
		BookAuthor bookAuthor = bookAuthorRepository.get(id);
		assertThat(bookAuthor).isNull();
	}
}