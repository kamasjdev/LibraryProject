package helpers.services.mappings;

import java.util.HashSet;

import dto.AuthorDto;
import dto.BillDto;
import dto.BookAuthorDto;
import dto.BookCustomerDto;
import dto.BookDto;
import dto.CustomerDto;
import entities.Author;
import entities.Bill;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import entities.Customer;

public class Mapper {
	public static AuthorDto mapToAuthorDto(Author author) {
		AuthorDto authorDto = new AuthorDto();
		authorDto.firstName = author.person.firstName;
		authorDto.lastName = author.person.lastName;
		authorDto.id = author.id;
		authorDto.books = new HashSet<BookAuthorDto>();
		return authorDto;
	}
	
	public static Author mapToAuthor(AuthorDto authorDto) {
		Author author = Author.create(authorDto.firstName, authorDto.lastName);
		author.id = authorDto.id;
		return author;
	}
	
	public static BookAuthorDto mapToBookAuthorDto(BookAuthor bookAuthor) {
		BookAuthorDto bookAuthorDto = new BookAuthorDto();
		bookAuthorDto.id = bookAuthor.id;
		bookAuthorDto.authorId = bookAuthor.authorId;
		bookAuthorDto.bookId = bookAuthor.bookId;
		return bookAuthorDto;
	}
	
	public static BookAuthor mapToBookAuthor(BookAuthorDto bookAuthorDto) {
		BookAuthor bookAuthor = BookAuthor.create(bookAuthorDto.bookId, bookAuthorDto.authorId);
		bookAuthor.id = bookAuthorDto.id;
		return bookAuthor;
	}
	
	public static BookDto mapToBookDto(Book book) {
		BookDto bookDto = new BookDto();
		bookDto.id = book.id;
		bookDto.bookCost = book.bookCost;
		bookDto.bookName = book.bookName;
		bookDto.ISBN = book.ISBN;
		bookDto.borrowed = book.borrowed;
		bookDto.authors = new HashSet<BookAuthorDto>();
		bookDto.customers = new HashSet<BookCustomerDto>();
		return bookDto;
	}
	
	public static Book mapToBook(BookDto bookDto) {
		Book book = Book.create(bookDto.bookName, bookDto.ISBN, bookDto.bookCost);
		book.id = bookDto.id;
		book.borrowed = bookDto.borrowed;
		book.authors = new HashSet<BookAuthor>();
		book.customers = new HashSet<BookCustomer>();
		return book;
	}
	
	public static BillDto mapToBillDto(Bill bill) {
		BillDto billDto = new BillDto();
		billDto.id = bill.id;
		billDto.cost = bill.cost;
		billDto.customerId = bill.customerId;
		return billDto;
	}
	
	public static Bill mapToBill(BillDto billDto) {
		Bill bill = Bill.create(billDto.cost, billDto.customerId);
		bill.id = bill.id;
		return bill;
	}
	
	public static BookCustomerDto mapToBookCustomerDto(BookCustomer bookCustomer) {
		BookCustomerDto bookCustomerDto = new BookCustomerDto();
		bookCustomerDto.id = bookCustomer.id;
		bookCustomerDto.customerId = bookCustomer.customerId;
		bookCustomerDto.bookId = bookCustomer.bookId;
		return bookCustomerDto;
	}
	
	public static BookCustomer mapToBookCustomer(BookCustomerDto bookCustomerDto) {
		BookCustomer bookCustomer = BookCustomer.create(bookCustomerDto.bookId, bookCustomerDto.customerId);
		bookCustomer.id = bookCustomerDto.id;
		return bookCustomer;
	}
	
	public static CustomerDto mapToCustomerDto(Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.id = customer.id;
		customerDto.firstName = customer.person.firstName;
		customerDto.lastName = customer.person.lastName;
		customerDto.limit = customer.limit;
		customerDto.canBorrow = customer.canBorrow;
		customerDto.books = new HashSet<BookCustomerDto>();
		customerDto.bills = new HashSet<BillDto>();
		return customerDto;
	}
	
	public static Customer mapToCustomer(CustomerDto customerDto) {
		Customer customer = Customer.create(customerDto.firstName, customerDto.lastName);
		customer.canBorrow = customerDto.canBorrow;
		customer.limit = customerDto.limit;
		customer.id = customerDto.id;
		return customer; 
	}
}
