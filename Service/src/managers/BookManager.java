package managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import entities.Author;
import entities.Bill;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import entities.Customer;
import interfaces.ActionService;
import services.AuthorService;
import services.BillService;
import services.BookAuthorService;
import services.BookCustomerService;
import services.BookService;
import services.CustomerService;

public class BookManager {
	private BookService bookService;
	private BookAuthorService bookAuthorService;
	private AuthorService authorService;
	private ActionService actionService;
	private CustomerService customerService;
	private BookCustomerService bookCustomerService;
	private BillService billService;
	
	public BookManager(BookService bookService, BookAuthorService bookAuthorService, AuthorService authorService, CustomerService customerService, BookCustomerService bookCustomerServicem, BillService billService, ActionService actionService) {
		this.bookService = bookService;
		this.bookAuthorService = bookAuthorService;
		this.authorService = authorService;
		this.actionService = actionService;
		this.customerService = customerService;
		this.billService = billService;
	}
	
	public void addBook(List<Author> authorsChosen) {
				
		if(authorsChosen.isEmpty()) {
			System.out.println("Chosen no authors");
			return;
		}
		
		System.out.println("Please enter name for book");
		String name = actionService.inputLine(String.class);
		System.out.println("Please enter ISBN");
		String ISBN = actionService.inputLine(String.class);
		System.out.println(String.format("Please enter cost for book %1$s", name));
		BigDecimal cost = actionService.inputLine(BigDecimal.class);
		Book book = Book.Create(name, ISBN, cost);
		bookService.add(book);
		for(Author auth : authorsChosen) {
			bookAuthorService.add(BookAuthor.Create(book.id, auth.id));
		}
		List<BookAuthor> bookAuthors = bookAuthorService.getBooksByBookId(book.id);
		book.authors = new HashSet<BookAuthor>(bookAuthors);
	}
	
	public void editBook(Integer bookId) {
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
		
		System.out.println("Do you want modify authors? Yes(Y) No(N)");
		String input = actionService.inputLine(String.class);
		
		if(!input.equals("Y") || !input.equals("N")) {
			System.out.println("Entered invalid string");
			return;
		}
		
		if(input.equals("Y")) {
			List<Integer> authorIds = new ArrayList<Integer>();
			Integer value = -1;
			while(value != 0) {
				System.out.println("Enter author id, 0 accept changes");
				value = actionService.inputLine(Integer.class);
				
				if(value < 0) {
					System.out.println("Entered invalid id");
					return;
				}
					
				authorIds.add(value);
			}

			if(!authorIds.isEmpty()) {
				List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
				authorIds.forEach(a -> {
					Author author = authorService.getById(a);
					
					if(author == null) {
						System.out.println(String.format("Author with id: %1$s doesnt exists", a));
						return;
					}
					
					bookAuthors.add(BookAuthor.Create(bookId, a));
				});
				
				List<BookAuthor> bookAuthorsExists = new ArrayList<BookAuthor>();
				boolean found = true;
				
				// find authors existed
				for(BookAuthor ba : book.authors) {
					for(BookAuthor b : bookAuthors) {
						if(b.authorId == ba.authorId) {
							found = true;
						}
						
						if(found) {
							bookAuthorsExists.add(ba);
							break;
						}
					}
				}
				
				List<BookAuthor> bookAuthorsCopy = new ArrayList<BookAuthor>(book.authors);
				List<BookAuthor> bookAuthorsTodelete = new ArrayList<BookAuthor>();
				
				// add authors to delete
				for(BookAuthor ba : bookAuthorsCopy) {
					BookAuthor bookAuthorTodelete = null;
					for(BookAuthor b : bookAuthors) { 
						if(ba.authorId == b.authorId) {
							bookAuthorTodelete = b;
							break;
						}
					}
					
					if(bookAuthorTodelete == null) {
						bookAuthorsTodelete.add(ba); 
					}
				}
				
				// remove existed authors
				for(BookAuthor ba : bookAuthorsExists) {	
					BookAuthor bookAuthor = bookAuthorsExists.stream().filter(b -> b.authorId == ba.authorId && b.bookId == bookId).findFirst().orElse(null);
					if(bookAuthor != null) {
						bookAuthors.remove(ba);
					}
				}  
				
				// First delete authors from books
				bookAuthorsTodelete.forEach(ba -> book.authors.remove(ba));
				
				// delete from service
				bookAuthorsTodelete.forEach(ba -> bookAuthorService.delete(ba.id));
				
				// Add new authors
				bookAuthors.forEach(ba -> {
					BookAuthor bookAuthor = bookAuthorService.getBookAuthor(b -> b.authorId == ba.authorId && b.bookId == bookId);
					bookAuthorService.add(bookAuthor);	
					book.authors.add(bookAuthor);
				});
			}
		}
		
		System.out.println("Enter book name, if dont need to change leave empty");
		String name = actionService.inputLine(String.class);
		
		if(!name.isEmpty()) {
			book.bookName = name;
		}
		
		System.out.println("Enter book name, if dont need to change put -1");
		BigDecimal cost = actionService.inputLine(BigDecimal.class);
		
		if(!cost.equals(new BigDecimal(-1))) {
			book.bookCost = cost;
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
		BookCustomer bookCustomer = BookCustomer.Create(bookId, customerId);
		bookCustomerService.add(bookCustomer);
		customer.books.add(bookCustomer);
		customerService.update(customer);
		Book book = bookService.getById(bookId);
		billService.add(Bill.Create(book.bookCost, customerId));
	}

	public void returnBook(Integer bookId, Integer customerId) {
		boolean borrowed = bookService.borrowed(bookId);
		
		if(!borrowed) {
			System.out.println(String.format("Book with id: %1$s was not borrowed", bookId));
		}
		
		BookCustomer bookCustomer = bookCustomerService.getBookCustomer(b->b.bookId==bookId && b.customerId == customerId);
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
}
