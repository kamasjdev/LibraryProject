package services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dto.BookAuthorDto;
import dto.BookCustomerDto;
import dto.BookDto;
import entities.Book;
import entities.BookAuthor;
import entities.BookCustomer;
import exceptions.service.book.BookCannotBeNullException;
import exceptions.service.book.BookISBNCannotBeEmptyException;
import exceptions.service.book.BookNameCannotBeEmptyException;
import exceptions.service.book.BookNotFoundException;
import exceptions.service.book.InvalidBookCostException;
import helpers.services.mappings.Mapper;
import interfaces.BaseService;
import interfaces.BookRepository;
import interfaces.BookService;

public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;	
	
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@Override
	public BookDto getById(Integer id) {
		Book book = bookRepository.get(id);
		
		if(book == null) {
			return null;
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
		
		BookDto bookDto = getById(dto.id);
		
		if(bookDto == null) {
			throw new BookNotFoundException(dto.id);
		}
		
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
		
		bookRepository.delete(book);
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
}