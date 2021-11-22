package tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import exceptions.jsonparser.CannotDeserializeObjectsException;
import services.AuthorService;
import services.JsonParser;

public class JsonParserTests {
	private AuthorService authorService;
	
	public JsonParserTests() {
		authorService = new AuthorService();
	}
	
	@Test
	public void given_valid_list_should_serialize_to_json() {
		Book book = Book.create("BookTest", "123456789", new BigDecimal(200.50));
		book.id = 1;
		Author author = Author.create("Mr", "Test");
		Author author2 = Author.create("Mrs", "Test");
		authorService.add(author);
		authorService.add(author2);
		BookAuthor bookAuthor = BookAuthor.create(book.id, author.id);
		BookAuthor bookAuthor2 = BookAuthor.create(book.id, author2.id);
		bookAuthor.id = 1;
		bookAuthor2.id = 2;
		author.books.add(bookAuthor);
		author2.books.add(bookAuthor2);
		
		String serializedObjects = JsonParser.serializeObjects(Author.class, authorService.getEntities());
		
		assertThat(serializedObjects).isNotEmpty();
	}
	
	@Test
	public void given_valid_string_should_deserialize_to_objects() {
		String jsonString = "[{\"id\":1,\"person\":{\"firstName\":\"Stanis³aw\",\"lastName\":\"Lem\"},\"books\":[]},{\"id\":2,\"person\":{\"firstName\":\"John Ronald Reuel\",\"lastName\":\"Tolkien\"},\"books\":[]}]"; 
		int expectedSize = 2;
		
		List<Author> authors = JsonParser.deserializeObjects(Author.class, jsonString);
		
		assertThat(authors).isNotNull();
		assertThat(authors).isNotEmpty();
		assertThat(authors.size()).isEqualTo(expectedSize);
	}
	
    @Test
    public void given_null_string_shouldnt_desrialize_to_objects() {
    	String jsonString = null;
    	
    	List<Author> authors = JsonParser.deserializeObjects(Author.class, jsonString);
    	
    	assertThat(authors).isNull();
    }
    
    @Test
    public void given_null_string_shouldnt_desrialize_to_object() {
    	String jsonString = null;
    	
    	Author author = JsonParser.deserializeObject(Author.class, jsonString);
    	
    	assertThat(author).isNull();
    }
    
    @Test
    public void given_invalid_json_should_throw_an_exception() {
    	String jsonString = "abc";
    	
    	CannotDeserializeObjectsException thrown = (CannotDeserializeObjectsException) catchThrowable(() -> JsonParser.deserializeObjects(Author.class, jsonString));
    	
    	assertThat(thrown).isNotNull();
    	assertThat(thrown).isInstanceOf(CannotDeserializeObjectsException.class)
				.hasMessage(String.format("Cannot deserialize objects of %1$s", Author.class));
    	assertThat(thrown.clazz).isEqualTo(Author.class);
    }
}
