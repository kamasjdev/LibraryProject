package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import common.BaseEntity;

@Entity
@Table(name = "bookauthor")
public class BookAuthor extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7996245169123357507L;

	@Column(name = "book_id")
	public Integer bookId;
	
	@ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
	public Book book;
	
	@Column(name = "author_id")
	public Integer authorId;
	
	@ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
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
}
