package entities;

import common.BaseEntity;

public class BookAuthor extends BaseEntity {
	public Integer bookId;
	public Book book;
	public Integer authorId;
	public Author author;
	
	public static BookAuthor create(Integer bookId, Integer authorId) {
		BookAuthor bookAuthor = new BookAuthor();
		bookAuthor.bookId = bookId;
		bookAuthor.authorId = authorId;
		
		return bookAuthor;
	}
	
	public static BookAuthor create(Integer bookId, Book book, Integer authorId, Author author) {
		BookAuthor bookAuthor = new BookAuthor();
		bookAuthor.bookId = bookId;
		bookAuthor.authorId = authorId;
		bookAuthor.book = book;
		bookAuthor.author = author;
		
		return bookAuthor;
	}
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder().append(id).append(". ").append(bookId)
				.append(" ").append(authorId);
		
		if(book != null) {
			description.append(" ").append(book);
		}
		
		if(author != null) {
			description.append(" ").append(author);
		}
		
		return description.toString();
	}
}
