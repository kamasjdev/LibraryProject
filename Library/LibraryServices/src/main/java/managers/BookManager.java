package managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import entities.Author;
import entities.Bill;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import helpers.manager.customer.UpdateCustomer;
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
	private final UpdateCustomer updateCustomer;
	
	public BookManager(BookService bookService, BookAuthorService bookAuthorService, AuthorService authorService, CustomerService customerService, BookCustomerService bookCustomerService, BillService billService, UpdateCustomer updateCustomer) {
		this.bookService = bookService;
		this.bookAuthorService = bookAuthorService;
		this.authorService = authorService;
		this.customerService = customerService;
		this.billService = billService;
		this.bookCustomerService = bookCustomerService;
		this.updateCustomer = updateCustomer;
	}
	
	public void addBook(String name, String ISBN, BigDecimal cost, List<Author> authorsChosen) {
				
		if(authorsChosen.isEmpty()) {
			System.out.println("Chosen no authors");
			return;
		}
		Book book = Book.create(name, ISBN, cost);
		authorsChosen = authorsChosen.stream().filter(distinctByKey(a -> a.id)).collect(Collectors.toList());
		
		for(Author author : authorsChosen) {
			BookAuthor bookAuthor = BookAuthor.create(null, author.id);
			book.authors.add(bookAuthor);
		}
		
		bookService.add(book);
	}
	
	public void editBook(Integer bookId, String bookName, String ISBN, BigDecimal bookCost, List<Integer> authorIds) {
		Book book = bookService.getById(bookId);
		
		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		int authorsCount = authorService.getCount();

		if(authorsCount == 0) {
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
			
			List<BookAuthor> bookAuthorsExists = updateCustomer.findAuthorsExistedInBook(book, bookAuthors);
			List<BookAuthor> bookAuthorsToDelete = updateCustomer.findAuthorsNotExistedInBook(book, bookAuthors);
			updateCustomer.removeExistedAuthors(bookAuthors, bookAuthorsExists);
						
			// delete from services
			bookAuthorsToDelete.forEach(ba -> {
				bookAuthorService.delete(ba.id);
			});
			
			// Add new authors
			bookAuthors.forEach(ba -> {
				bookAuthorService.add(ba);
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
		Book book = bookService.getDetails(bookId);
		
		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		System.out.println("Book: ");
		System.out.println(book);
	}

	public void borrowBook(Integer bookId, Integer customerId) {
		boolean canBorrow = customerService.canBorrow(customerId);
		
		if(!canBorrow) {
			System.out.println(String.format("Customer with id: %1$s cant borrow any book", customerId));
			return;
		}
		
		BookCustomer bookCustomer = BookCustomer.create(bookId, customerId);
		bookCustomerService.add(bookCustomer);
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
		
		BookCustomer bookCustomer = bookCustomerService.getBookCustomerByBookIdAndCustomerId(bookId, customerId);
		bookCustomerService.delete(bookCustomer.id);
		Book book = bookService.getById(bookId);
		book.borrowed = false;
		bookService.update(book);
	}

	public void deleteBook(Integer bookId) {
		Book book = bookService.getDetails(bookId);

		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		if(!book.customers.isEmpty()) {
			System.out.println("Cannot delete book when is borrowed");
			return;
		}
		
		List<BookAuthor> bookAuthors = bookAuthorService.getBooksByBookId(bookId);
		bookAuthors.forEach(ba -> {
			bookAuthorService.delete(ba.id);
		});
		bookService.delete(bookId);
	}
	
	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
}
