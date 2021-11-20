package tests;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import entities.Author;
import services.AuthorService;

public class AuthorServiceTests {

	private AuthorService authorService;
	
	public AuthorServiceTests() {
		authorService = new AuthorService();
	}
	
	@Test
	public void given_valid_id_should_return_author() {
		Author author = Author.Create("Jan", "Kowal");
		Integer id = authorService.Add(author);
		
		Author auth = authorService.GetById(id);
		
		assertThat(auth.person.firstName).isEqualTo(author.person.firstName);
		assertThat(auth.person.lastName).isEqualTo(author.person.lastName);
	}
	
	@Test
	public void given_invalid_id_should_return_null() {
		Integer id = null;
		
		Author auth = authorService.GetById(id);
		
		assertThat(auth).isEqualTo(null);
	}
	
	@Test
	public void given_two_authors_get_last_id_should_return_last_id() {
		Author author = Author.Create("Mr", "Test");
		Author author2 = Author.Create("Mrs", "Test");
		authorService.Add(author);
		authorService.Add(author2);
		Integer expectedId = 3;
		
		Integer id = authorService.GetLastId();
		
		assertThat(id).isEqualTo(expectedId);
	}
}
