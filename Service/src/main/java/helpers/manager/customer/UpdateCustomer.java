package helpers.manager.customer;

import java.util.ArrayList;
import java.util.List;

import entities.Book;
import entities.BookAuthor;

public class UpdateCustomer {

	public List<BookAuthor> findAuthorsExistedInBook(Book book, List<BookAuthor> bookAuthors) {
		List<BookAuthor> bookAuthorsExists = new ArrayList<BookAuthor>();
		boolean found = true;
		
		for(BookAuthor ba : book.authors) {
			for(BookAuthor b : bookAuthors) {
				if(b.authorId.equals(ba.authorId)) {
					found = true;
				}
				
				if(found) {
					bookAuthorsExists.add(ba);
					break;
				}
			}
		}
		
		return bookAuthorsExists;
	}
	
	public List<BookAuthor> findAuthorsNotExistedInBook(Book book, List<BookAuthor> bookAuthors) {
		List<BookAuthor> bookAuthorsNotExisted = new ArrayList<BookAuthor>();
		
		// add authors to delete
		for(BookAuthor ba : book.authors) {
			BookAuthor bookAuthorToDelete = null;
			for(BookAuthor b : bookAuthors) { 
				if(ba.authorId.equals(b.authorId)) {
					bookAuthorToDelete = b;
					break;
				}
			}
			
			if(bookAuthorToDelete == null) {
				bookAuthorsNotExisted.add(ba); 
			}
		}
		
		return bookAuthorsNotExisted;
	}

	public void removeExistedAuthors(List<BookAuthor> bookAuthors, List<BookAuthor> bookAuthorsExists) {
		// remove existed authors
		for(BookAuthor ba : bookAuthorsExists) {	
			BookAuthor bookAuthor = bookAuthors.stream().filter(b -> b.authorId.equals(ba.authorId) && b.bookId.equals(ba.bookId)).findFirst().orElse(null);
			if(bookAuthor != null) {
				bookAuthors.remove(bookAuthor);
			}
		}  
	}

}
