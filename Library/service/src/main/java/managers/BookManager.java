package managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import dto.AuthorDto;
import dto.BillDto;
import dto.BookAuthorDto;
import dto.BookCustomerDto;
import dto.BookDto;
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
	
	public void addBook(String name, String ISBN, BigDecimal cost, List<AuthorDto> authorsChosen) {
				
		if(authorsChosen.isEmpty()) {
			System.out.println("Chosen no authors");
			return;
		}
		
		BookDto bookDto = new BookDto();
		bookDto.bookName = name;
		bookDto.ISBN = ISBN;
		bookDto.bookCost = cost;
		authorsChosen = authorsChosen.stream().filter(distinctByKey(a -> a.id)).collect(Collectors.toList());
		
		Integer bookId = bookService.add(bookDto);
				
		for(AuthorDto author : authorsChosen) {
			BookAuthorDto bookAuthorDto = new BookAuthorDto();
			bookAuthorDto.authorId = author.id;
			bookAuthorDto.bookId = bookId;
			bookAuthorService.add(bookAuthorDto);
		}
	}
	
	public void editBook(Integer bookId, String bookName, String ISBN, BigDecimal bookCost, List<Integer> authorIds) {
		BookDto bookDto = bookService.getById(bookId);
		
		if(bookDto == null) {
			System.out.println("Book not found");
			return;
		}
		
		int authorsCount = authorService.getCount();

		if(authorsCount == 0) {
			System.out.println("Before add book please first add some authors");
			return;
		}

		if(!authorIds.isEmpty()) {
			List<BookAuthorDto> bookAuthorsDto = new ArrayList<BookAuthorDto>();
			authorIds.forEach(a -> {
				AuthorDto authorDto = authorService.getById(a);
				
				if(authorDto == null) {
					System.out.println(String.format("Author with id: %1$s doesnt exists", a));
					return;
				}
		
				BookAuthorDto bookAuthorDto = new BookAuthorDto();
				bookAuthorDto.bookId = bookId;
				bookAuthorDto.authorId = a;
				bookAuthorsDto.add(bookAuthorDto);
			});
			
			List<BookAuthorDto> bookAuthorsExists = updateCustomer.findAuthorsExistedInBook(bookDto, bookAuthorsDto);
			List<BookAuthorDto> bookAuthorsToDelete = updateCustomer.findAuthorsNotExistedInBook(bookDto, bookAuthorsDto);
			updateCustomer.removeExistedAuthors(bookAuthorsDto, bookAuthorsExists);
						
			// delete from services
			bookAuthorsToDelete.forEach(ba -> {
				bookAuthorService.delete(ba.id);
			});
			
			// Add new authors
			bookAuthorsDto.forEach(ba -> {
				bookAuthorService.add(ba);
			});
		}
		
		if(!bookName.isEmpty()) {
			bookDto.bookName = bookName;
		}
		
		if(!ISBN.isEmpty()) {
			bookDto.ISBN = ISBN;
		}
		
		if(!bookCost.equals(new BigDecimal(-1))) {
			bookDto.bookCost = bookCost;
		}
		
		bookService.update(bookDto);
	}

	public List<BookDto> getAll() {
		List<BookDto> booksDto = bookService.getEntities();
		return booksDto;
	}

	public void getBookDetails(Integer bookId) {
		BookDto bookDto = bookService.getDetails(bookId);
		
		if(bookDto == null) {
			System.out.println("Book not found");
			return;
		}
		
		System.out.println("Book: ");
		System.out.println(bookDto);
	}

	public void borrowBook(Integer bookId, Integer customerId) {
		boolean canBorrow = customerService.canBorrow(customerId);
		
		if(!canBorrow) {
			System.out.println(String.format("Customer with id: %1$s cant borrow any book", customerId));
			return;
		}
		
		BookCustomerDto bookCustomerDto = new BookCustomerDto();
		bookCustomerDto.bookId = bookId;
		bookCustomerDto.customerId = customerId;
		bookCustomerService.add(bookCustomerDto);
		
		BookDto bookDto = bookService.getById(bookId);
		bookDto.borrowed = true;
		bookService.update(bookDto);
		
		BillDto billDto = new BillDto();
		billDto.cost = bookDto.bookCost;
		billDto.customerId = customerId;
		billService.add(billDto);
	}

	public void returnBook(Integer bookId, Integer customerId) {
		boolean borrowed = bookService.borrowed(bookId);
		
		if(!borrowed) {
			System.out.println(String.format("Book with id: %1$s was not borrowed", bookId));
			return;
		}
		
		BookCustomerDto bookCustomerDto = bookCustomerService.getBookCustomerByBookIdAndCustomerId(bookId, customerId);
		bookCustomerService.delete(bookCustomerDto.id);
		
		BookDto bookDto = bookService.getById(bookId);
		bookDto.borrowed = false;
		bookService.update(bookDto);
	}

	public void deleteBook(Integer bookId) {
		BookDto bookDto = bookService.getDetails(bookId);

		if(bookDto == null) {
			System.out.println("Book not found");
			return;
		}
		
		if(!bookDto.customers.isEmpty()) {
			System.out.println("Cannot delete book when is borrowed");
			return;
		}
		
		List<BookAuthorDto> bookAuthorsDto = bookAuthorService.getBooksByBookId(bookId);
		bookAuthorsDto.forEach(ba -> {
			bookAuthorService.delete(ba.id);
		});
		bookService.delete(bookId);
	}
	
	private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
}