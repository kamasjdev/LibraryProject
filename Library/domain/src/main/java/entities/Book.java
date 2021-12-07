package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import common.BaseEntity;

@Entity
@Table(name = "books")
public class Book extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -6277725971413764872L;

	public Book() {
		authors = new HashSet<BookAuthor>();
		customers = new HashSet<BookCustomer>();
	}
	
	@Column(name = "book_name")
	public String bookName;
	
	@Column(name = "isbn")
	public String ISBN;
	
	@Column(name = "cost")
	public BigDecimal bookCost;
	
	@Column(name = "borrowed")
	public boolean borrowed;
	
	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
	public Set<BookAuthor> authors;
	
	@OneToMany(mappedBy = "book")
	public Set<BookCustomer> customers;
	
	public static Book create(String bookName, String ISBN, BigDecimal bookCost) {
		Book book = new Book();
		book.bookName = bookName;
		book.ISBN = ISBN;
		book.bookCost = bookCost;
		book.borrowed = false;
		
		return book;
	}
	
	public static Book create(String bookName, String ISBN, BigDecimal bookCost, Set<BookAuthor> authors) {
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
}
