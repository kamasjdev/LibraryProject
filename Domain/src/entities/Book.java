package entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.BaseEntity;

public class Book extends BaseEntity {
	public Book() {
		authors = new ArrayList<BookAuthor>();
		customers = new HashSet<BookCustomer>();
	}
	
	public String bookName;
	public String ISBN;
	public BigDecimal bookCost;
	public boolean borrowed;
	
	@JsonDeserialize(as = List.class)
	@JsonSerialize(as = List.class)
	public List<BookAuthor> authors;
	
	@JsonDeserialize(as = HashSet.class)
	@JsonSerialize(as = HashSet.class)
	public HashSet<BookCustomer> customers;
	
	public static Book create(String bookName, String ISBN, BigDecimal bookCost) {
		Book book = new Book();
		book.bookName = bookName;
		book.ISBN = ISBN;
		book.bookCost = bookCost;
		book.borrowed = false;
		
		return book;
	}
	
	public static Book create(String bookName, String ISBN, BigDecimal bookCost, List<BookAuthor> authors) {
		Book book = new Book();
		book.bookName = bookName;
		book.ISBN = ISBN;
		book.bookCost = bookCost;
		book.borrowed = false;
		
		if(authors != null && !authors.isEmpty()) {
			book.authors = authors;
		}
		
		return book;
	}
	
	@Override
	public String toString() {
		StringBuilder description = new StringBuilder().append(id).append(". ").append(bookName)
				.append(" ").append(bookCost).append(" ").append(borrowed);
		
		if(!authors.isEmpty()) {
			description.append("\nBook authors:\n");
			
			for(BookAuthor bookAuthor : authors) {
				description.append(bookAuthor);
				description.append("\n");
			}
		}
		
		if(!customers.isEmpty()) {
			description.append("\nBorrowed by customers:\n");
			
			for(BookCustomer bookCustomer : customers) {
				description.append(bookCustomer);
				description.append("\n");
			}
		}
		
		return description.toString();
	}
}
