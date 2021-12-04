package mappings;

import java.util.List;
import java.util.Map;

import entities.BookAuthor;
import exceptions.mappings.bookauthor.CannotFindBookAuthorFieldException;
import interfaces.MapEntity;

public class BookAuthorMapping implements MapEntity<BookAuthor> {

	@Override
	public BookAuthor Map(List<Map<String, Object>> fields) {
		BookAuthor bookAuthor = null;
		
		if(fields == null) {
			return bookAuthor;
		}
		
		if(fields.isEmpty()) {
			return bookAuthor;
		}	
		
		bookAuthor = new BookAuthor();
		
		Object id = fields.stream().filter(f -> f.containsKey("bookauthor.Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bookauthor.Id");
		}).get("bookauthor.Id");
		
		bookAuthor.id = (Integer) id;
		
		Object authorId = fields.stream().filter(f -> f.containsKey("bookauthor.Author_Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bookauthor.Author_Id");
		}).get("bookauthor.Author_Id");
		
		bookAuthor.authorId = (Integer) authorId;
		
		Object bookId = fields.stream().filter(f -> f.containsKey("bookauthor.Book_Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindBookAuthorFieldException("bookauthor.Book_Id");
		}).get("bookauthor.Book_Id");
		
		bookAuthor.bookId = (Integer) bookId;
		
		if(bookAuthor.id == null) {
			return null;
		}
		
		return bookAuthor;
	}

}
