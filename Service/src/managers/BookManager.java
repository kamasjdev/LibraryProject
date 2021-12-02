package managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entities.Author;
import entities.Bill;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import entities.Customer;
import services.AuthorService;
import services.BillService;
import services.BookAuthorService;
import services.BookCustomerService;
import services.BookService;
import services.CustomerService;

public class BookManager {
	private final BookService bookService;
	private final BookAuthorService bookAuthorService;
	private final AuthorService authorService;
	private final CustomerService customerService;
	private final BookCustomerService bookCustomerService;
	private final BillService billService;
	
	public BookManager(BookService bookService, BookAuthorService bookAuthorService, AuthorService authorService, CustomerService customerService, BookCustomerService bookCustomerService, BillService billService) {
		this.bookService = bookService;
		this.bookAuthorService = bookAuthorService;
		this.authorService = authorService;
		this.customerService = customerService;
		this.billService = billService;
		this.bookCustomerService = bookCustomerService;
	}
	
	public void addBook(String name, String ISBN, BigDecimal cost, List<Author> authorsChosen) {
				
		if(authorsChosen.isEmpty()) {
			System.out.println("Chosen no authors");
			return;
		}
		Book book = Book.create(name, ISBN, cost);
		bookService.add(book);
		for(Author auth : authorsChosen) {
			Integer bookAuthorId = bookAuthorService.add(BookAuthor.create(book.id, auth.id));
			auth.books.add(bookAuthorService.getById(bookAuthorId));
		}
		List<BookAuthor> bookAuthors = bookAuthorService.getBooksByBookId(book.id);
		book.authors = new ArrayList<BookAuthor>(bookAuthors);
	}
	
	public void editBook(Integer bookId, String bookName, String ISBN, BigDecimal bookCost, List<Integer> authorIds) {
		Book book = bookService.getById(bookId);
		
		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		List<Author> authors = authorService.getEntities();

		if(authors.isEmpty()) {
			System.out.println("Before add book please first add some authors");
			return;
		}

		if(!authorIds.isEmpty()) {
			List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
			authorIds.forEach(a -> {
				Author author = authorService.getById(a);
				
				if(author == null) {
					System.out.println(String.format("Author with id: %1$s doesnt exists", a));
					return;
				}
		
				bookAuthors.add(BookAuthor.create(bookId, a));
			});
			
			List<BookAuthor> bookAuthorsExists = findAuthorsExistedInBook(book, bookAuthors);
			List<BookAuthor> bookAuthorsToDelete = findAuthorsNotExistedInBook(book, bookAuthors);
			
			// remove existed authors
			for(BookAuthor ba : bookAuthorsExists) {	
				BookAuthor bookAuthor = bookAuthors.stream().filter(b -> b.authorId.equals(ba.authorId) && b.bookId.equals(bookId)).findFirst().orElse(null);
				if(bookAuthor != null) {
					bookAuthors.remove(bookAuthor);
				}
			}  
			
			// First delete authors from books
			bookAuthorsToDelete.forEach(ba -> book.authors.remove(ba));
			
			// delete from services
			bookAuthorsToDelete.forEach(ba -> {
				bookAuthorService.delete(ba.id);
				Author author = authorService.getById(ba.authorId);
				author.books.remove(ba);
			});
			
			// Add new authors
			bookAuthors.forEach(ba -> {
				bookAuthorService.add(ba);	
				book.authors.add(ba);
				Author author = authorService.getById(ba.authorId);
				author.books.add(ba);
			});
		}
		
		if(!bookName.isEmpty()) {
			book.bookName = bookName;
		}
		
		if(!bookCost.equals(new BigDecimal(-1))) {
			book.bookCost = bookCost;
		}
		
		bookService.update(book);
	}

	public List<Book> getAll() {
		List<Book> books = bookService.getEntities();
		return books;
	}

	public void getBookDetails(Integer bookId) {
		Book book = bookService.getById(bookId);
		
		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		List<Integer> authorList = book.authors.stream().map(b->b.authorId).collect(Collectors.toList());
		List<Author> authors = authorService.getEntities().stream().filter(b->authorList.stream().anyMatch(bl->bl.equals(b.id))).collect(Collectors.toList());
		System.out.println("Book: ");
		System.out.println(book);
		System.out.println("Author's of book: ");
		authors.forEach(a->System.out.println(a));
	}

	public void borrowBook(Integer bookId, Integer customerId) {
		boolean canBorrow = customerService.canBorrow(customerId);
		
		if(!canBorrow) {
			System.out.println(String.format("Customer with id: %1$s cant borrow any book", customerId));
			return;
		}
		
		Customer customer = customerService.getById(customerId);		
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		bookCustomerService.add(bookCustomer);
		customer.books.add(bookCustomer);
		customerService.update(customer);
		Book book = bookService.getById(bookId);
		book.borrowed = true;
		bookService.update(book);
		billService.add(Bill.create(book.bookCost, customerId));
	}

	public void returnBook(Integer bookId, Integer customerId) {
		boolean borrowed = bookService.borrowed(bookId);
		
		if(!borrowed) {
			System.out.println(String.format("Book with id: %1$s was not borrowed", bookId));
			return;
		}
		
		BookCustomer bookCustomer = bookCustomerService.getBookCustomer(b->b.bookId.equals(bookId) && b.customerId.equals(customerId));
		Customer customer = customerService.getById(customerId);
		customer.books.remove(bookCustomer);
		customerService.update(customer);
		bookCustomerService.delete(bookCustomer.id);
		Book book = bookService.getById(bookId);
		book.borrowed = false;
		bookService.update(book);
	}

	public void deleteBook(Integer bookId) {
		Book book = bookService.getById(bookId);

		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		if(book.borrowed) {
			System.out.println("Cannot delete book when is borrowed");
			return;
		}
		
		List<BookAuthor> bookAuthors = bookAuthorService.getBooksByBookId(bookId);
		bookAuthors.forEach(ba -> {
			Author author = authorService.getById(ba.authorId);
			author.books.remove(ba);
			bookAuthorService.delete(ba.id);
		});
		bookService.delete(bookId);
	}
	
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
}
