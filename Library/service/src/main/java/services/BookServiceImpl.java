package services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.AuthorDto;
import dto.BillDto;
import dto.BookAuthorDto;
import dto.BookCustomerDto;
import dto.BookDto;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import exceptions.service.author.AuthorNotFoundException;
import exceptions.service.book.AuthorCountShouldntBeZeroException;
import exceptions.service.book.BookCannotBeNullException;
import exceptions.service.book.BookISBNCannotBeEmptyException;
import exceptions.service.book.BookNameCannotBeEmptyException;
import exceptions.service.book.BookNotFoundException;
import exceptions.service.book.CustomerCannotBorrowBookException;
import exceptions.service.book.InvalidBookCostException;
import helpers.manager.customer.UpdateCustomer;
import helpers.services.mappings.Mapper;
import interfaces.AuthorService;
import interfaces.BillService;
import interfaces.BookAuthorService;
import interfaces.BookCustomerService;
import interfaces.BookService;
import interfaces.CustomerService;
import repository.BookRepository;

@Service
@Transactional
public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;	
	private final AuthorService authorService;
	private final BookAuthorService bookAuthorService;
	private final UpdateCustomer updateCustomer;
	private final CustomerService customerService;
	private final BookCustomerService bookCustomerService;
	private final BillService billService;
	private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	
	public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, BookAuthorService bookAuthorService, UpdateCustomer updateCustomer, CustomerService customerService, BookCustomerService bookCustomerService, BillService billService) {
		this.bookRepository = bookRepository;
		this.authorService = authorService;
		this.bookAuthorService = bookAuthorService;
		this.updateCustomer = updateCustomer;
		this.customerService = customerService;
		this.bookCustomerService = bookCustomerService;
		this.billService = billService;
	}
	
	@Override
	public BookDto getById(Integer id) {
		Book book = bookRepository.get(id);
		
		if(book == null) {
			throw new BookNotFoundException(id);
		}
		
		BookDto bookDto = Mapper.mapToBookDto(book);
		addAuthors(bookDto, book.authors);
		
		return bookDto;
	}

	@Override
	public List<BookDto> getAll() {
		List<Book> books = bookRepository.getAll();
		List<BookDto> booksDto = new ArrayList<BookDto>();
		
		for(Book book : books) {
			BookDto bookDto = Mapper.mapToBookDto(book);
			addAuthors(bookDto, book.authors);
			booksDto.add(bookDto);
		}
		
		return booksDto;
	}

	@Override
	public void update(BookDto dto) {
		validateBook(dto);
		
		int authorsCount = authorService.getCount();

		if(authorsCount == 0) {
			throw new AuthorCountShouldntBeZeroException(); 
		}
		
		BookDto bookDto = getById(dto.id);
		
		if(bookDto == null) {
			throw new BookNotFoundException(dto.id);
		}
		
		updateAuthorsInBook(bookDto, dto);
		
		bookDto.bookCost = dto.bookCost;
		bookDto.bookName = dto.bookName;
		bookDto.ISBN = dto.ISBN;
		bookDto.borrowed = dto.borrowed;
		Book book = Mapper.mapToBook(bookDto);
		bookRepository.update(book);
	}

	@Override
	public Integer add(BookDto dto) {
		validateBook(dto);
		int authorsCount = authorService.getCount();

		if(authorsCount == 0) {
			throw new AuthorCountShouldntBeZeroException(); 
		}
		
		Book book = Book.create(dto.bookName, dto.ISBN, dto.bookCost);

		Integer id = bookRepository.add(book);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		Book book = get(id);
		
		if(book == null) {
			throw new BookNotFoundException(id);
		}
		
		bookRepository.delete(book.id);
	}
	

	private void validateBook(BookDto book) {
		if(book == null) {
			throw new BookCannotBeNullException();
		}
		
		if(book.bookName.isEmpty()) {
			throw new BookNameCannotBeEmptyException(book.id);
		}
		
		if(book.ISBN.isEmpty()) {
			throw new BookISBNCannotBeEmptyException(book.id);
		}
		
		if(book.bookCost.compareTo(BigDecimal.ZERO) == -1) { // First BigDecimal is less than Second BigDecimal
			throw new InvalidBookCostException(book.id, book.bookCost);
		}
	}

	public boolean borrowed(Integer bookId) {
		BookDto bookDto = getById(bookId);
		
		if(bookDto == null) {
			throw new BookNotFoundException(bookId);
		}
		
		boolean borrowed = bookDto.borrowed;
		return borrowed;
	}

	@Override
	public BookDto getDetails(Integer bookId) {
		Book book = bookRepository.getBookDetails(bookId);
		
		if(book == null) {
			return null;
		}
		
		BookDto bookDto = Mapper.mapToBookDto(book);
		addAuthors(bookDto, book.authors);
		
		for(BookCustomer bookCustomer : book.customers) {
			BookCustomerDto bookCustomerDto = Mapper.mapToBookCustomerDto(bookCustomer);
			bookCustomerDto.customer = Mapper.mapToCustomerDto(bookCustomer.customer);
			bookDto.customers.add(bookCustomerDto);
		}
			
		return bookDto;
	}
	
	private Book get(Integer id) {
		Book book = bookRepository.getBookWithoutAuthors(id);
		return book;
	}
	
	private void addAuthors(BookDto bookDto, Set<BookAuthor> bookAuthors) {
		for(BookAuthor bookAuthor : bookAuthors) {
			BookAuthorDto bookAuthorDto = Mapper.mapToBookAuthorDto(bookAuthor);
			bookAuthorDto.author = Mapper.mapToAuthorDto(bookAuthor.author);
			bookDto.authors.add(bookAuthorDto);
		}
	}

	private void updateAuthorsInBook(BookDto bookDto, BookDto bookDtoToChange) {
		if(!bookDtoToChange.authors.isEmpty()) {
			List<BookAuthorDto> bookAuthorsDto = new ArrayList<BookAuthorDto>();
			bookDtoToChange.authors.forEach(a -> {
				AuthorDto authorDto = authorService.getById(a.authorId);
				
				if(authorDto == null) {
					throw new AuthorNotFoundException(a.authorId); 
				}
		
				BookAuthorDto bookAuthorDto = new BookAuthorDto();
				bookAuthorDto.bookId = bookDtoToChange.id;
				bookAuthorDto.authorId = a.authorId;
				bookAuthorsDto.add(bookAuthorDto);
			});
			
			List<BookAuthorDto> bookAuthorsExists = updateCustomer.findAuthorsExistedInBook(bookDto, bookAuthorsDto);
			List<BookAuthorDto> bookAuthorsToDelete = updateCustomer.findAuthorsNotExistedInBook(bookDto, bookAuthorsDto);
			updateCustomer.removeExistedAuthors(bookAuthorsDto, bookAuthorsExists);
						
			for(BookAuthorDto bookAuthor : bookAuthorsToDelete) {
				logger.info("BookAuthor to delete " + bookAuthor.id);
			}
			
			// delete from services
			bookAuthorsToDelete.forEach(ba -> {
				bookAuthorService.delete(ba.id);
			});
			
			// Add new authors
			bookAuthorsDto.forEach(ba -> {
				bookAuthorService.add(ba);
			});
		}
	}

	@Override
	public void borrowBook(Integer id, Integer customerId) {
		boolean canBorrow = customerService.canBorrow(customerId);
		
		if(!canBorrow) {
			throw new CustomerCannotBorrowBookException(customerId);
		}
		
		BookCustomerDto bookCustomerDto = new BookCustomerDto();
		bookCustomerDto.bookId = id;
		bookCustomerDto.customerId = customerId;
		bookCustomerService.add(bookCustomerDto);
		
		BookDto bookDto = getById(id);
		bookDto.borrowed = true;
		update(bookDto);
		
		BillDto billDto = new BillDto();
		billDto.cost = bookDto.bookCost;
		billDto.customerId = customerId;
		billService.add(billDto);
	}

	@Override
	public void returnBook(Integer id, Integer customerId) {		
		BookCustomerDto bookCustomerDto = bookCustomerService.getBookCustomerByBookIdAndCustomerId(id, customerId);
		bookCustomerService.delete(bookCustomerDto.id);
		
		BookDto bookDto = getById(id);
		bookDto.borrowed = false;
		update(bookDto);
	}

	@Override
	public void deleteBookWithBookAuthors(int bookId) {
		bookRepository.deleteBookWithBookAuthors(bookId);
	}
}