package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import entities.Author;
import interfaces.AuthorRepository;

public class AuthorRepositoryTests {
	private AuthorRepository authorRepository; 
	
	public AuthorRepositoryTests() {
		SessionManager.createSessionManager("hibernateTest.cfg.xml");
		authorRepository = new AuthorRepositoryImpl(SessionManager.getSessionFactory());
	}
	
	@Test
	public void should_return_all_authors() {
		List<Author> authors = authorRepository.getAll();
		
		assertThat(authors.size()).isGreaterThan(0);
	}
	
	@Test
	public void should_return_author_details() {
		Integer id = 1;
		
		Author author = authorRepository.getAuthorDetails(id);
		
		assertThat(author).isNotNull();
	}
	
	@Test
	public void given_valid_id_should_return_author() {
		Integer id = 1;
		
		Author author = authorRepository.get(id);
		
		assertThat(author).isNotNull();
		assertThat(author.id).isEqualTo(id);
	}
	
	@Test
	public void given_invalid_id_should_return_null_author() {
		Integer id = -1;
		
		Author author = authorRepository.get(id);
		
		assertThat(author).isNull();	
	}
	
	@Test
	public void given_valid_parameters_should_add_author() {
		Author author = Author.create("AuthorB", "AuthorC");
		
		Integer id = authorRepository.add(author);
		
		assertThat(id).isNotNull();
		assertThat(id).isGreaterThan(0);
	}
	
	@Test
	public void given_valid_parameters_should_update_author() {
		Integer id = 11;
		String firstName = "Imie";
		String lastName = "Nazwisko";
		Author author = new Author();
		author.id = id;
		author.person.firstName = firstName;
		author.person.lastName = lastName;
		
		authorRepository.update(author);
		Author authorUpdated = authorRepository.get(id);
		
		assertThat(authorUpdated).isNotNull();
		assertThat(authorUpdated.person.firstName).isEqualTo(firstName);
		assertThat(authorUpdated.person.lastName).isEqualTo(lastName);
	}
	
	@Test
	public void given_valid_parameters_should_delete_author() {
		Integer id = 11;
		
		authorRepository.delete(id);
		Author authorUpdated = authorRepository.get(id);
		
		assertThat(authorUpdated).isNull();
	}
}
