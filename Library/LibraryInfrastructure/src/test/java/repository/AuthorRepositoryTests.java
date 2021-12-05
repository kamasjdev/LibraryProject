package repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import entities.Author;
import interfaces.AuthorRepository;

public class AuthorRepositoryTests {
	private AuthorRepository authorRepository; 
	
	public AuthorRepositoryTests() {
		authorRepository = new AuthorRepositoryImpl("hibernate.cfg.xml");
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
}
