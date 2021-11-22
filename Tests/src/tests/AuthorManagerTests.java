package tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import entities.Author;
import interfaces.ActionService;
import managers.AuthorManager;
import services.AuthorService;
import services.BookService;

@RunWith(MockitoJUnitRunner.class)
public class AuthorManagerTests {
	private AuthorManager authorManager;
	private AuthorService authorService;
	private BookService bookService;
	private ActionService actionService;
	
	public AuthorManagerTests() {
		authorService = new AuthorService();
		bookService = new BookService();
		actionService = Mockito.mock(ActionService.class);
		authorManager = new AuthorManager(authorService, bookService, actionService);
	}
	
	@Test
	public void given_valid_parameters_should_add_author() {
		String firstName = "FirstName";
		String lastName = "LastName";
		when(actionService.inputLine(String.class)).thenReturn(firstName).thenReturn(lastName);
		int expectedSize = 1;
		
		authorManager.addAuthor();
		List<Author> authors = authorService.getEntities();
		
		assertThat(authors.size()).isEqualTo(expectedSize);
		Author author = authors.stream().findFirst().get();
		assertThat(author.person.firstName).isEqualTo(firstName);
		assertThat(author.person.lastName).isEqualTo(lastName);
	}
}
