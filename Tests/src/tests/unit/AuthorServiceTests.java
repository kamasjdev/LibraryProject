package tests.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;

import org.junit.Test;

import entities.Author;
import exceptions.service.author.AuthorCannotBeNullException;
import exceptions.service.author.AuthorFirstNameCannotBeEmptyException;
import exceptions.service.author.AuthorLastNameCannotBeEmptyException;
import exceptions.service.author.AuthorNotFoundException;
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
	public void given_invalid_author_when_add_should_throw_an_exception() {
		Author author = null;
		AuthorCannotBeNullException expectedException = new AuthorCannotBeNullException();
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_author_first_name_when_add_should_throw_an_exception() {
		Author author = new Author();
		AuthorFirstNameCannotBeEmptyException expectedException = new AuthorFirstNameCannotBeEmptyException(author.id);
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
	}
	
	@Test
	public void given_invalid_author_last_name_when_add_should_throw_an_exception() {
		Author author = new Author();
		author.person.firstName = "Test";
		AuthorLastNameCannotBeEmptyException expectedException = new AuthorLastNameCannotBeEmptyException(author.id);
		
		Throwable thrown = catchThrowable(() -> authorService.add(author));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
					.hasMessage(expectedException.getMessage());
		assertThat(((AuthorLastNameCannotBeEmptyException) thrown).authorId).isEqualTo(author.id);
	}
	
	@Test
	public void given_valid_author_should_add() {
		Author author = Author.create("Imie", "Nazwisko");
		Integer expectedId = 1;
		
		Integer authorId = authorService.add(author);
		
		assertThat(authorId).isEqualTo(expectedId);
	}
	
	@Test
	public void given_valid_author_should_update_author() {
		Author author = Author.create("Imie", "Nazwisko");
		Integer authorId = authorService.add(author);
		String firstName = "abc";
		
		Author auth = authorService.getById(authorId);
		auth.person.firstName = firstName;
		authorService.update(auth);
		Author authorUpdated = authorService.getById(authorId);
		
		assertThat(authorUpdated).isNotNull();
		assertThat(authorUpdated.person.firstName).isEqualTo(firstName);
	}
	
	@Test
	public void given_invalid_first_name_when_update_should_throw_an_exception() {
		Author author = Author.create("Imie", "Nazwisko");
		Integer authorId = authorService.add(author);
		String firstName = null;
		AuthorFirstNameCannotBeEmptyException expectedException = new AuthorFirstNameCannotBeEmptyException(authorId);

		Author auth = authorService.getById(authorId);
		auth.person.firstName = firstName;
		Throwable thrown = catchThrowable(() -> authorService.update(auth));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
			.hasMessage(expectedException.getMessage());
		assertThat(((AuthorFirstNameCannotBeEmptyException) thrown).authorId).isEqualTo(author.id);
	}

	@Test
	public void given_invalid_last_name_when_update_should_throw_an_exception() {
		Author author = Author.create("Imie", "Nazwisko");
		Integer authorId = authorService.add(author);
		String lastName = null;
		AuthorLastNameCannotBeEmptyException expectedException = new AuthorLastNameCannotBeEmptyException(authorId);
		
		Author auth = authorService.getById(authorId);
		auth.person.lastName = lastName;
		Throwable thrown = catchThrowable(() -> authorService.update(auth));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
			.hasMessage(expectedException.getMessage());
		assertThat(((AuthorLastNameCannotBeEmptyException) thrown).authorId).isEqualTo(author.id);
	}
	
	@Test
	public void given_invalid_id_when_delete_should_throw_an_exception() {
		Integer id = 1;
		AuthorNotFoundException expectedException = new AuthorNotFoundException(id);
		
		Throwable thrown = catchThrowable(() -> authorService.delete(id));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
			.hasMessage(expectedException.getMessage());
		assertThat(((AuthorNotFoundException) thrown).authorId).isEqualTo(id);
	}
	
	@Test
	public void given_valid_id_should_delete_author() {
		Author author = Author.create("First", "Last");
		Integer authorId = authorService.add(author);
		int expectedSize = 0;
		
		authorService.delete(authorId);
		List<Author> authors = authorService.getEntities();
		
		assertThat(authors.size()).isEqualTo(expectedSize);
	}
}
