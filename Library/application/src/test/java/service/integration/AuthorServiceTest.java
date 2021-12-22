package service.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dto.AuthorDto;
import exceptions.service.author.AuthorNotFoundException;
import interfaces.AuthorService;
import repository.configuration.PersistenceConfig;
import service.configuration.ServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class, ServiceConfiguration.class } )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Sql({"classpath:init.sql"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorServiceTest {
	
	@Autowired
	private AuthorService authorService;
	
	@AfterEach
	public void afterEach() {
		
	}
	
	@Test
	public void given_author_should_add_to_db() {
		AuthorDto authorDto = createAuthor();
		
		Integer id = authorService.add(authorDto);
		
		assertThat(id).isGreaterThan(0);
	}
	
	@Test
	public void given_id_should_return_author() {
		Integer id = 3;
		
		AuthorDto author = authorService.getById(id);
		
		assertThat(author).isNotNull();
	}
	
	@Test
	public void given_author_should_update() {
		Integer id = 2;
		String firstName = "Mixing";
		AuthorDto authorDto = authorService.getById(id);
		authorDto.firstName = firstName;
		
		authorService.update(authorDto);
		
		AuthorDto authorUpdated = authorService.getById(id);
		assertThat(authorUpdated).isNotNull();
		assertThat(authorUpdated.firstName).isEqualTo(firstName);
	}
	
	@Test
	public void should_return_authors() {
		List<AuthorDto> authors = authorService.getAll();

		assertThat(authors).isNotNull();
		assertThat(authors.size()).isGreaterThan(0);
	}
	
	@Test
	public void given_id_should_delete_author() {
		AuthorDto authorDto = createAuthor();		
		Integer id = authorService.add(authorDto);
		
		authorService.delete(id);
		
		AuthorDto authorUpdatedDto = authorService.getById(id);
		assertThat(authorUpdatedDto).isNull();
	}
	
	@Test
	public void given_invalid_id_should_delete_author() {
		Integer id = 100;
		AuthorNotFoundException expectedException = new AuthorNotFoundException(id);
		
		AuthorNotFoundException thrown = (AuthorNotFoundException) catchThrowable(() -> authorService.delete(id));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass())
			.hasMessage(expectedException.getMessage());
		assertThat(thrown.authorId).isEqualTo(id);
	}
	
	private AuthorDto createAuthor() {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = "Andrew";
		authorDto.lastName = "McAfrey";
		return authorDto;		
	}
}
