package tests;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import entities.Author;
import exceptions.service.author.AuthorCannotBeNullException;
import exceptions.service.author.AuthorFirstNameCannotBeEmptyException;
import exceptions.service.author.AuthorLastNameCannotBeEmptyException;
import services.AuthorService;

public class AuthorServiceTests {

	private AuthorService authorService;
	
	public AuthorServiceTests() {
		authorService = new AuthorService();
	}
	
	@Test
	public void given_valid_id_should_return_author() {
		Author author = Author.create("Jan", "Kowal");
		Integer id = authorService.add(author);
		
		Author auth = authorService.getById(id);
		
		assertThat(auth.person.firstName).isEqualTo(author.person.firstName);
		assertThat(auth.person.lastName).isEqualTo(author.person.lastName);
	}
	
	@Test
	public void given_invalid_id_should_return_null() {
		Integer id = null;
		
		Author auth = authorService.getById(id);
		
		assertThat(auth).isEqualTo(null);
	}
	
	@Test
	public void given_two_authors_should_return_last_id() {
		Author author = Author.create("Mr", "Test");
		Author author2 = Author.create("Mrs", "Test");
		authorService.add(author);
		authorService.add(author2);
		Integer expectedId = 3;
		
		Integer id = authorService.getLastId();
		
		assertThat(id).isEqualTo(expectedId);
	}
	
	@Test
	public void given_invalid_author_should_throw_an_exception() {
		Author author = null;
		AuthorCannotBeNullException expectedException = new AuthorCannotBeNullException();
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_author_first_name_should_throw_an_exception() {
		Author author = new Author();
		AuthorFirstNameCannotBeEmptyException expectedException = new AuthorFirstNameCannotBeEmptyException(author.id);
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_author_last_name_should_throw_an_exception() {
		Author author = new Author();
		author.person.firstName = "Test";
		AuthorLastNameCannotBeEmptyException expectedException = new AuthorLastNameCannotBeEmptyException(author.id);
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
	}
	
	@Test
	public void given_valid_author_should_add() {
		Author author = Author.create("Imie", "Nazwisko");
		Integer expectedId = 1;
		
		Integer authorId = authorService.add(author);
		
		assertThat(authorId).isEqualTo(expectedId);
	}
}
