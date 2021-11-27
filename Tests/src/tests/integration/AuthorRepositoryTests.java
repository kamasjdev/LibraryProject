package tests.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import connection.db.DbClientImpl;
import connection.db.DbConnectionImpl;
import entities.Author;
import interfaces.AuthorRepository;
import interfaces.DbClient;
import interfaces.DbConnection;
import interfaces.MapEntity;
import mappings.AuthorMapping;
import repository.AuthorRepositoryImpl;

public class AuthorRepositoryTests {
	private AuthorRepository authorRepository;
	private MapEntity<Author> mapper;
	private DbClient dbClient;
	private DbConnection dbConnection;
	private String connectionString = "jdbc:mysql://localhost:3306/library?user=root&password=";
	private String dbPackage = "com.mysql.cj.jdbc.Driver";	
	
	public AuthorRepositoryTests() {
		dbConnection = new DbConnectionImpl(connectionString, dbPackage);
		dbClient = new DbClientImpl(dbConnection);
		mapper = new AuthorMapping();
		authorRepository = new AuthorRepositoryImpl(dbClient, mapper);
	}
	
	@Test
	public void given_valid_parameters_should_return_list_author() {
		List<Author> authors = authorRepository.getAll();
		
		assertThat(authors).isNotNull();
		assertThat(authors.size()).isGreaterThan(0);
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
		Integer id = 6;
		String firstName = "Imie";
		String lastName = "Nazwisko";
		Author author = new Author();
		author.id = 6;
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
		Integer id = 6;
		
		authorRepository.delete(id);
		Author authorUpdated = authorRepository.get(id);
		
		assertThat(authorUpdated).isNull();
	}
}
