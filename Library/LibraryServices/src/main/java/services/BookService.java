package services;

import java.math.BigDecimal;
import java.util.List;

import entities.Book;
import exceptions.service.book.BookCannotBeNullException;
import exceptions.service.book.BookISBNCannotBeEmptyException;
import exceptions.service.book.BookNameCannotBeEmptyException;
import exceptions.service.book.BookNotFoundException;
import exceptions.service.book.InvalidBookCostException;
import interfaces.BaseService;
import interfaces.BookRepository;

public class BookService implements BaseService<Book> {
	private final BookRepository bookRepository;	
	
	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@Override
	public Book getById(Integer id) {
		Book book = bookRepository.get(id);
		return book;
	}

	@Override
	public List<Book> getEntities() {
		List<Book> books = bookRepository.getAll();
		return books;
	}

	@Override
	public void update(Book entity) {
		validateBook(entity);
		
		Book book = bookRepository.get(entity.id);
		
		if(book == null) {
			throw new BookNotFoundException(entity.id);
		}
		
		book.bookCost = entity.bookCost;
		book.bookName = entity.bookName;
		book.ISBN = entity.ISBN;
		bookRepository.update(entity);
	}

	@Override
	public Integer add(Book entity) {
		validateBook(entity);

		Integer id = bookRepository.add(entity);
		
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
	

	private void validateBook(Book book) {
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
		Book book = getById(bookId);
		
		if(book == null) {
			throw new BookNotFoundException(bookId);
		}
		
		boolean borrowed = book.borrowed;
		return borrowed;
	}

	@Override
	public int getCount() {
		int count = bookRepository.getCount();
		return count;
	}

	public Book getDetails(Integer bookId) {
		Book book = bookRepository.getBookDetails(bookId);
		return book;
	}
	
	private Book get(Integer id) {
		Book book = bookRepository.getBookWithoutAuthors(id);
		return book;
	}
}
