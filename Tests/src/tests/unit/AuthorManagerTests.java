package tests.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import entities.Author;
import managers.AuthorManager;
import services.AuthorService;

@RunWith(MockitoJUnitRunner.class)
public class AuthorManagerTests {
	private AuthorManager authorManager;
	private AuthorService authorService;
	
	public AuthorManagerTests() {
		authorService = Mockito.mock(AuthorService.class);
		authorManager = new AuthorManager(authorService);
	}
	
	@Test
	public void given_valid_parameters_should_add_author() {
		String firstName = "FirstName";
		String lastName = "LastName";
		int expectedUse = 1;
		
		authorManager.addAuthor(firstName, lastName);
		
		verify(authorService, times(expectedUse)).add(any(Author.class));
	}
	
	@Test
	public void given_valid_parameters_should_show_author_details() {
		Integer id = 1;
		String firstName = "Abc";
		String lastName = "Test";
		Author author = Author.create(firstName, lastName);
		author.id = 1;
		when(authorService.getById(id)).thenReturn(author);
		int expectedUse = 1;
		
		authorManager.getAuthorDetails(id);
		
		verify(authorService, times(expectedUse)).getById(id);
	}

	@Test
	public void given_valid_parameters_should_delete_author() {
		Integer id = 1;
		String firstName = "Abc";
		String lastName = "Test";
		Author author = Author.create(firstName, lastName);
		author.id = 1;
		when(authorService.getById(id)).thenReturn(author);
		int expectedUse = 1;
		
		authorManager.deleteAuthor(id);
		
		verify(authorService, times(expectedUse)).getById(id);
		verify(authorService, times(expectedUse)).delete(id);
	}
	
	@Test
	public void given_valid_parameters_should_update_author() {
		Integer id = 1;
		String firstName = "Abc";
		String lastName = "Test";
		Author author = Author.create(firstName, lastName);
		author.id = 1;
		when(authorService.getById(id)).thenReturn(author);
		int expectedUse = 1;
		
		authorManager.editAuthor(id, firstName, lastName);
		
		verify(authorService, times(expectedUse)).getById(id);
		verify(authorService, times(expectedUse)).update(author);
	}
}
