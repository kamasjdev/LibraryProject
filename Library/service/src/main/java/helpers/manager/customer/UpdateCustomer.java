package helpers.manager.customer;

import java.util.ArrayList;
import java.util.List;

import dto.BookAuthorDto;
import dto.BookDto;
import entities.Book;
import entities.BookAuthor;

public class UpdateCustomer {

	public List<BookAuthorDto> findAuthorsExistedInBook(BookDto bookDto, List<BookAuthorDto> bookAuthorsDto) {
		List<BookAuthorDto> bookAuthorsDtoExists = new ArrayList<BookAuthorDto>();
		boolean found = true;
		
		for(BookAuthorDto ba : bookDto.authors) {
			for(BookAuthorDto b : bookAuthorsDto) {
				if(b.authorId.equals(ba.authorId)) {
					found = true;
				}
				
				if(found) {
					bookAuthorsDtoExists.add(ba);
					break;
				}
			}
		}
		
		return bookAuthorsDtoExists;
	}
	
	public List<BookAuthorDto> findAuthorsNotExistedInBook(BookDto bookDto, List<BookAuthorDto> bookAuthorsDto) {
		List<BookAuthorDto> bookAuthorsDtoNotExisted = new ArrayList<BookAuthorDto>();
		
		// add authors to delete
		for(BookAuthorDto ba : bookDto.authors) {
			BookAuthorDto bookAuthorToDelete = null;
			for(BookAuthorDto b : bookAuthorsDto) { 
				if(ba.authorId.equals(b.authorId)) {
					bookAuthorToDelete = b;
					break;
				}
			}
			
			if(bookAuthorToDelete == null) {
				bookAuthorsDtoNotExisted.add(ba); 
			}
		}
		
		return bookAuthorsDtoNotExisted;
	}

	public void removeExistedAuthors(List<BookAuthorDto> bookAuthorsDto, List<BookAuthorDto> bookAuthorsDtoExists) {
		// remove existed authors
		for(BookAuthorDto ba : bookAuthorsDtoExists) {	
			BookAuthorDto bookAuthorDto = bookAuthorsDto.stream().filter(b -> b.authorId.equals(ba.authorId) && b.bookId.equals(ba.bookId)).findFirst().orElse(null);
			if(bookAuthorDto != null) {
				bookAuthorsDto.remove(bookAuthorDto);
			}
		}  
	}

}
