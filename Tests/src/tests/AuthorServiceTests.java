package tests;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
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
	public void given_two_authors_should_return_last_id() {
		Author author = Author.Create("Mr", "Test");
		Author author2 = Author.Create("Mrs", "Test");
		authorService.Add(author);
		authorService.Add(author2);
		Integer expectedId = 3;
		
		Integer id = authorService.GetLastId();
		
		assertThat(id).isEqualTo(expectedId);
	}
	
	@Test
	public void given_valid_list_should_serialize_to_json() {
		Book book = Book.Create("BookTest", "123456789", new BigDecimal(200.50));
		book.id = 1;
		Author author = Author.Create("Mr", "Test");
		Author author2 = Author.Create("Mrs", "Test");
		authorService.Add(author);
		authorService.Add(author2);
		BookAuthor bookAuthor = BookAuthor.Create(book.id, author.id);
		BookAuthor bookAuthor2 = BookAuthor.Create(book.id, author2.id);
		bookAuthor.id = 1;
		bookAuthor2.id = 2;
		author.books.add(bookAuthor);
		author2.books.add(bookAuthor2);
		
		String serializedObjects = authorService.SerializeObjects();
		
		assertThat(serializedObjects).isNotEmpty();
	}
	
	@Test
	public void given_valid_string_should_deserialize_to_objects() {
		String jsonString = "[{\"id\":1,\"person\":{\"firstName\":\"Mr\",\"lastName\":\"Test\"},\"books\":[{\"id\":1,\"bookId\":1,\"authorId\":1}]},{\"id\":2,\"person\":{\"firstName\":\"Mrs\",\"lastName\":\"Test\"},\"books\":[{\"id\":2,\"bookId\":1,\"authorId\":2}]}]";
		int expectedSize = 2;
		
		authorService.DeserializeObjects(jsonString);
		List<Author> authors = authorService.GetEntities();
		
		assertThat(authors).isNotNull();
		assertThat(authors).isNotEmpty();
		assertThat(authors.size()).isEqualTo(expectedSize);
	}
}
