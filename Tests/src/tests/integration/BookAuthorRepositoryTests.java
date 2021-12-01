package tests.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import interfaces.BookAuthorRepository;
import interfaces.MapEntity;
import mappings.AuthorMapping;
import mappings.BookAuthorMapping;
import mappings.BookMapping;
import repository.BookAuthorRepositoryImpl;

public class BookAuthorRepositoryTests extends BaseTest {
	private BookAuthorRepository bookAuthorRepository;
	private MapEntity<BookAuthor> mapper;
	private MapEntity<Book> bookMapper;
	private MapEntity<Author> authorMapper;
	
	public BookAuthorRepositoryTests() {
		this.mapper = new BookAuthorMapping();
		this.bookMapper = new BookMapping();
		this.authorMapper = new AuthorMapping();
		this.bookAuthorRepository = new BookAuthorRepositoryImpl(dbClient, mapper, bookMapper, authorMapper);
	}
	
	@Test
	public void given_valid_parameters_should_return_list_book_author() {
		List<BookAuthor> bookAuthors = bookAuthorRepository.getAll();
		
		assertThat(bookAuthors).isNotNull();
		assertThat(bookAuthors.size()).isGreaterThan(0);
	}
}
