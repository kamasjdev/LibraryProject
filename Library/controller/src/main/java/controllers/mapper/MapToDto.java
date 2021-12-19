package controllers.mapper;

import java.util.HashSet;

import controllers.pojos.author.AddAuthor;
import controllers.pojos.author.UpdateAuthor;
import controllers.pojos.book.AddBook;
import controllers.pojos.book.UpdateBook;
import controllers.pojos.customer.AddCustomer;
import controllers.pojos.customer.UpdateCustomer;
import dto.AuthorDto;
import dto.BookAuthorDto;
import dto.BookDto;
import dto.CustomerDto;

public class MapToDto {
	public static AuthorDto mapAddAuthorToAuthorDto(AddAuthor addAuthor) {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = addAuthor.firstName;
		authorDto.lastName = addAuthor.lastName;
		authorDto.id = 0;
		return authorDto;
	}
	
	public static AuthorDto mapUpdateAuthorToAuthorDto(UpdateAuthor updateAuthor) {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = updateAuthor.firstName;
		authorDto.lastName = updateAuthor.lastName;
		authorDto.id = updateAuthor.id;
		return authorDto;
	}
	
	public static CustomerDto mapAddCustomerToCustomerDto(AddCustomer addCustomer) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.firstName = addCustomer.firstName;
		customerDto.lastName = addCustomer.lastName;
		customerDto.limit = 0;
		return customerDto;
	}
	
	public static CustomerDto mapUpdateCustomerToCustomerDto(UpdateCustomer updateCustomer) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.firstName = updateCustomer.firstName;
		customerDto.lastName = updateCustomer.lastName;
		customerDto.limit = updateCustomer.limit;
		customerDto.canBorrow = updateCustomer.canBorrow;
		customerDto.id = updateCustomer.id;
		return customerDto;
	}
	
	public static BookDto mapAddBookToBookDto(AddBook addBook) {
		BookDto bookDto = new BookDto();
		bookDto.bookName = addBook.bookName;
		bookDto.ISBN = addBook.ISBN;
		bookDto.bookCost = addBook.bookCost;
		bookDto.authors = new HashSet<BookAuthorDto>();
		return bookDto;
	}
	
	public static BookDto mapUpdateBookToBookDto(UpdateBook updateBook) {
		BookDto bookDto = new BookDto();		
		bookDto.id = updateBook.id;
		bookDto.bookName = updateBook.bookName;
		bookDto.bookCost = updateBook.bookCost;
		bookDto.ISBN = updateBook.ISBN;
		bookDto.authors = new HashSet<BookAuthorDto>();
		updateBook.authorsIds.forEach(a -> {
			BookAuthorDto bookAuthorDto = new BookAuthorDto();
			bookAuthorDto.authorId = a;
			bookDto.authors.add(bookAuthorDto);
		});
		return bookDto;
	}
}
