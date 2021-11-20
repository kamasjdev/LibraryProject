package services;

import java.math.BigDecimal;
import java.util.List;

import abstracts.AbstractBaseService;
import entities.Book;
import exceptions.service.book.BookCannotBeNullException;
import exceptions.service.book.BookISBNCannotBeEmptyException;
import exceptions.service.book.BookNameCannotBeEmptyException;
import exceptions.service.book.BookNotFoundException;
import exceptions.service.book.InvalidBookCostException;

public class BookService extends AbstractBaseService<Book> {
		
	@Override
	public Book GetById(Integer id) {
		Book book = objects.stream().filter(o->o.id.equals(id)).findFirst().orElse(null);
		return book;
	}

	@Override
	public List<Book> GetEntities() {
		return objects;
	}

	@Override
	public void Update(Book entity) {
		validateBook(entity);
		
		Book book = GetById(entity.id);
		
		if(book == null) {
			throw new BookNotFoundException(entity.id);
		}
		
		book.bookCost = entity.bookCost;
		book.bookName = entity.bookName;
		book.ISBN = entity.ISBN;
	}

	@Override
	public Integer Add(Book entity) {
		validateBook(entity);
		
		Integer id = GetLastId();
		entity.id = id;
		objects.add(entity);
		
		return id;
	}

	@Override
	public void Delete(Integer id) {
		Book book = GetById(id);
		
		if(book == null) {
			throw new BookNotFoundException(id);
		}
		
		objects.remove(book);
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

	public boolean Borrowed(Integer bookId) {
		Book book = GetById(bookId);
		
		if(book == null) {
			throw new BookNotFoundException(bookId);
		}
		
		boolean borrowed = book.borrowed;
		return borrowed;
	}
}
