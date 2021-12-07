package dto;

public class BookAuthorDto extends BaseDto {
	public Integer bookId;
	public BookDto book;
	public Integer authorId;
	public AuthorDto author;
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder().append(id).append(". ").append(bookId)
				.append(" ").append(authorId);
		
		if(book != null) {
			description.append("\nBook: ").append(book);
		}
		
		if(author != null) {
			description.append("\nAuthor ").append(author);
		}
		
		return description.toString();
	}
}
