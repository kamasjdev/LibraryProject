package entities;

import common.BaseEntity;

public class BookAuthor extends BaseEntity {
	public Integer bookId;
	public Integer authorId;
	
	public static BookAuthor create(Integer bookId, Integer authorId) {
		BookAuthor bookAuthor = new BookAuthor();
		bookAuthor.bookId = bookId;
		bookAuthor.authorId = authorId;
		
		return bookAuthor;
	}
	
	@Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s", id, bookId, authorId);
		return description;
	}
}
