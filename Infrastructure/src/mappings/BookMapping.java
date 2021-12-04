package mappings;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import entities.Book;
import exceptions.mappings.author.CannotFindAuthorFieldException;
import interfaces.MapEntity;

public class BookMapping implements MapEntity<Book> {

	@Override
	public Book Map(List<Map<String, Object>> fields) {
		Book book = null;
		
		if(fields == null) {
			return book;
		}
		
		if(fields.isEmpty()) {
			return book;
		}	
		
		book = new Book();
		
		Object id = fields.stream().filter(f -> f.containsKey("books.Id")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("books.Id");
		}).get("books.Id");
		
		book.id = (Integer) id;
		
		Object ISBN = fields.stream().filter(f -> f.containsKey("books.ISBN")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("books.ISBN");
		}).get("books.ISBN");
		
		book.ISBN = (String) ISBN;
		
		Object bookName = fields.stream().filter(f -> f.containsKey("books.Book_name")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("books.Book_name");
		}).get("books.Book_name");
		
		book.bookName = (String) bookName;
		
		Object cost = fields.stream().filter(f -> f.containsKey("books.cost")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("books.cost");
		}).get("books.cost");
		
		book.bookCost = (BigDecimal) cost;

		Object borrowed = fields.stream().filter(f -> f.containsKey("books.borrowed")).findFirst().orElseThrow(() -> {
			throw new CannotFindAuthorFieldException("books.borrowed");
		}).get("books.borrowed");
		
		book.borrowed = false;
		
		if(borrowed != null) {
			book.borrowed = (boolean) borrowed; 
		}
		
		if(book.id == null) {
			return null;
		}
		
		return book;
	}

}
