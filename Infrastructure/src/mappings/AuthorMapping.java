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
		
		if(fields != null) {
			author = new Author();
			
			Object id = fields.stream().filter(f -> f.containsKey("Id")).findFirst().orElseThrow(() -> {
				throw new CannotFindAuthorFieldException();
			}).get("Id");
			
			author.id = (Integer) id;
			
			Object firstName = fields.stream().filter(f -> f.containsKey("FIRST_NAME")).findFirst().orElseThrow(() -> {
				throw new CannotFindAuthorFieldException();
			}).get("FIRST_NAME");
			
			author.person.firstName = (String) firstName;
			
			Object lastName = fields.stream().filter(f -> f.containsKey("LAST_NAME")).findFirst().orElseThrow(() -> {
				throw new CannotFindAuthorFieldException();
			}).get("LAST_NAME");
			
			author.person.lastName = (String) lastName;
		}
		
		return author; 
	}
	
}
