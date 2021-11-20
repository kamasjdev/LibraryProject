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
	
	public void AddBook(List<Author> authorsChosen) {
				
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
		bookService.Add(book);
		for(Author auth : authorsChosen) {
			bookAuthorService.Add(BookAuthor.Create(book.id, auth.id));
		}
		List<BookAuthor> bookAuthors = bookAuthorService.GetBooksByBookId(book.id);
		book.authors = new HashSet<BookAuthor>(bookAuthors);
	}
	
	public void EditBook(Integer bookId) {
		Book book = bookService.GetById(bookId);
		
		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		List<Author> authors = authorService.GetEntities();

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
					Author author = authorService.GetById(a);
					
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
				List<BookAuthor> bookAuthorsToDelete = new ArrayList<BookAuthor>();
				
				// add authors to delete
				for(BookAuthor ba : bookAuthorsCopy) {
					BookAuthor bookAuthorToDelete = null;
					for(BookAuthor b : bookAuthors) { 
						if(ba.authorId == b.authorId) {
							bookAuthorToDelete = b;
							break;
						}
					}
					
					if(bookAuthorToDelete == null) {
						bookAuthorsToDelete.add(ba); 
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
				bookAuthorsToDelete.forEach(ba -> book.authors.remove(ba));
				
				// Delete from service
				bookAuthorsToDelete.forEach(ba -> bookAuthorService.Delete(ba.id));
				
				// Add new authors
				bookAuthors.forEach(ba -> {
					BookAuthor bookAuthor = bookAuthorService.GetBookAuthor(b -> b.authorId == ba.authorId && b.bookId == bookId);
					bookAuthorService.Add(bookAuthor);	
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
		
		bookService.Update(book);
	}

	public List<Book> GetAll() {
		List<Book> books = bookService.GetEntities();
		return books;
	}

	public void GetBookDetails(Integer bookId) {
		Book book = bookService.GetById(bookId);
		
		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		List<Integer> authorList = book.authors.stream().map(b->b.authorId).collect(Collectors.toList());
		List<Author> authors = authorService.GetEntities().stream().filter(b->authorList.stream().anyMatch(bl->bl.equals(b.id))).collect(Collectors.toList());
		System.out.println("Book: ");
		System.out.println(book);
		System.out.println("Author's of book: ");
		authors.forEach(a->System.out.println(a));
	}

	public void BorrowBook(Integer bookId, Integer customerId) {
		boolean canBorrow = customerService.CanBorrow(customerId);
		
		if(!canBorrow) {
			System.out.println(String.format("Customer with id: %1$s cant borrow any book", customerId));
			return;
		}
		
		Customer customer = customerService.GetById(customerId);		
		BookCustomer bookCustomer = BookCustomer.Create(bookId, customerId);
		bookCustomerService.Add(bookCustomer);
		customer.books.add(bookCustomer);
		customerService.Update(customer);
		Book book = bookService.GetById(bookId);
		billService.Add(Bill.Create(book.bookCost, customerId));
	}

	public void ReturnBook(Integer bookId, Integer customerId) {
		boolean borrowed = bookService.Borrowed(bookId);
		
		if(!borrowed) {
			System.out.println(String.format("Book with id: %1$s was not borrowed", bookId));
		}
		
		BookCustomer bookCustomer = bookCustomerService.GetBookCustomer(b->b.bookId==bookId && b.customerId == customerId);
		Customer customer = customerService.GetById(customerId);
		customer.books.remove(bookCustomer);
		customerService.Update(customer);
		bookCustomerService.Delete(bookCustomer.id);
		Book book = bookService.GetById(bookId);
		book.borrowed = false;
		bookService.Update(book);
	}

	public void DeleteBook(Integer bookId) {
		Book book = bookService.GetById(bookId);

		if(book == null) {
			System.out.println("Book not found");
			return;
		}
		
		if(book.borrowed) {
			System.out.println("Cannot delete book when is borrowed");
			return;
		}
		
		List<BookAuthor> bookAuthors = bookAuthorService.GetBooksByBookId(bookId);
		bookAuthors.forEach(ba -> {
			Author author = authorService.GetById(ba.authorId);
			author.books.remove(ba);
			bookAuthorService.Delete(ba.id);
		});
		bookService.Delete(bookId);
	}
}
