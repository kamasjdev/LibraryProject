package service.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dto.AuthorDto;
import interfaces.AuthorService;
import repository.configuration.PersistenceConfig;
import service.configuration.ServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class, ServiceConfiguration.class } )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Sql({"classpath:init.sql"})
public class AuthorServiceTest {
	
	@Autowired
	private AuthorService authorService;
	
	@Test
	public void given_author_should_add_to_db() {
		AuthorDto authorDto = createAuthor();
		
		Integer id = authorService.add(authorDto);
		
		assertThat(id).isGreaterThan(0);
	}
	
	private AuthorDto createAuthor() {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = "Andrew";
		authorDto.lastName = "McAfrey";
		return authorDto;		
	}
}
