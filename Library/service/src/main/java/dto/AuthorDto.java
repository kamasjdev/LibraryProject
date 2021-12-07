package dto;

import java.util.Set;

public class AuthorDto extends BaseDto {
	public String firstName;
	public String lastName;
	public Set<BookAuthorDto> books;
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder().append(id).append(". ").append(firstName)
				.append(" ").append(lastName);
		
		if(!books.isEmpty()) {
			description.append("\nAuthor of books:\n");
			
			for(BookAuthorDto bookAuthor : books) {
				description.append(bookAuthor);
				description.append("\n");
			}
		}
		
		return description.toString();
	}
}
