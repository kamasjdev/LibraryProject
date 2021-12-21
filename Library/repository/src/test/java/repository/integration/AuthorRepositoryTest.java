package repository.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import entities.Author;
import repository.AuthorRepository;
import repository.configuration.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Transactional
@Sql({"classpath:init.sql"})
public class AuthorRepositoryTest {
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Test
	public void given_valid_id_should_return_author_from_db() {
		Integer id = 2;
		
		Author author = authorRepository.get(id);
		
		assertThat(author).isNotNull();
		assertThat(author.id).isEqualTo(id);
	}
	
	@Test
	public void given_author_should_add_to_db() {
		String firstName = "Mr";
		String lastName = "Test";
		Author author = Author.create(firstName, lastName);
		
		Integer id = authorRepository.add(author);
		
		Author authorFromDb = authorRepository.get(id);
		assertThat(authorFromDb).isNotNull();
		assertThat(authorFromDb.id).isEqualTo(id);
	}
	
	@Test
	public void given_authors_from_init_should_return_more_than_one_author() {
		int size = 3;
		
		List<Author> authors = authorRepository.getAll();
		
		assertThat(authors.size()).isGreaterThan(0);
		assertThat(authors.size()).isEqualTo(size);
	}
	
	@Test
	public void given_valid_author_should_delete_from_db() {
		String firstName = "Mr";
		String lastName = "Test";
		Author author = Author.create(firstName, lastName);		
		Integer id = authorRepository.add(author);
		
		authorRepository.delete(id);
		
		Author authorFromDb = authorRepository.get(id);
		assertThat(authorFromDb).isNull();
	}
	
	@Test
	public void given_valid_author_should_update() {
		Integer id = 1;
		String lastName = "Tester123";
		Author author = authorRepository.get(id);
		author.person.lastName = lastName;
		
		authorRepository.update(author);
		
		Author authorUpdated = authorRepository.get(id);
		assertThat(authorUpdated.person.lastName).isEqualTo(lastName);
	}
}
