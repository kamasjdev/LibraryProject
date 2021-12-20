package repository.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import entities.Author;
import repository.AuthorRepository;
import repository.configuration.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Transactional
public class AuthorRepositoryTest {
	
	@Autowired
	private AuthorRepository authorRepository;
	
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
}
