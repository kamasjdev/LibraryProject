package entities;

import java.math.BigDecimal;
import java.util.HashSet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.BaseEntity;

public class Book extends BaseEntity {
	public Book() {
		authors = new HashSet<BookAuthor>();
	}
	
	public String bookName;
	public String ISBN;
	public BigDecimal bookCost;
	public boolean borrowed;
	
	@JsonDeserialize(as = HashSet.class)
	@JsonSerialize(as = HashSet.class)
	public HashSet<BookAuthor> authors;
	
	public static Book create(String bookName, String ISBN, BigDecimal bookCost) {
		Book book = new Book();
		book.bookName = bookName;
		book.ISBN = ISBN;
		book.bookCost = bookCost;
		book.borrowed = false;
		
		return book;
	}
	
	public static Book Create(String bookName, String ISBN, BigDecimal bookCost, HashSet<BookAuthor> authors) {
		Book book = new Book();
		book.bookName = bookName;
		book.ISBN = ISBN;
		book.bookCost = bookCost;
		book.borrowed = false;
		
		if(!authors.isEmpty()) {
			book.authors = authors;
		}
		
		return book;
	}
	
	@Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s %4$s %5$s", id, bookName, ISBN, bookCost, borrowed);
		return description;
	}
}
