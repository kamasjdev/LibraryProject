package mappings;

import java.util.List;
import java.util.Map;

import entities.Author;
import exceptions.mappings.author.CannotFindAuthorFieldException;
import interfaces.MapEntity;

public class AuthorMapping implements MapEntity<Author> {

	@Override
	public Author Map(List<Map<String, Object>> fields) {
		Author author = null;
		
		if(fields == null) {
			return author;
		}
		
		if(fields.isEmpty()) {
			return author;
		}	
		
		author = new Author();
		
		Object id = fields.stream().filter(f -> f.containsKey("authors.Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("authors.Id");
		}).get("authors.Id");
		
		author.id = (Integer) id;
		
		Object firstName = fields.stream().filter(f -> f.containsKey("authors.FIRST_NAME")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("authors.FIRST_NAME");
		}).get("authors.FIRST_NAME");
		
		author.person.firstName = (String) firstName;
		
		Object lastName = fields.stream().filter(f -> f.containsKey("authors.LAST_NAME")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("authors.LAST_NAME");
		}).get("authors.LAST_NAME");
		
		author.person.lastName = (String) lastName;
		
		return author; 
	}
	
}
