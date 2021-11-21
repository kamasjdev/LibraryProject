package services;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import abstracts.AbstractBaseService;
import entities.BookAuthor;
import exceptions.service.bookauthor.BookAuthorCannotBeNullException;
import exceptions.service.bookauthor.BookAuthorNotFoundException;
import exceptions.service.bookauthor.InvalidBookAuthorAuthorIdException;
import exceptions.service.bookauthor.InvalidBookAuthorBookIdException;

public class BookAuthorService extends AbstractBaseService<BookAuthor> {
	
	@Override
	public BookAuthor getById(Integer id) {
		BookAuthor bookAuthor = objects.stream().filter(o->o.id.equals(id)).findFirst().orElse(null);
		return bookAuthor;
	}

	@Override
	public List<BookAuthor> getEntities() {
		return objects;
	}

	@Override
	public void update(BookAuthor entity) {
		validateBookAuthor(entity);
		
		BookAuthor bookAuthor = getById(entity.id);
		
		if(bookAuthor == null) {
			throw new BookAuthorNotFoundException(entity.id);
		}
		
		bookAuthor.authorId = entity.authorId;
		bookAuthor.bookId = entity.bookId;
	}

	@Override
	public Integer add(BookAuthor entity) {
		validateBookAuthor(entity);
		
		Integer id = getLastId();
		entity.id = id;
		objects.add(entity);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		BookAuthor bookAuthor = getById(id);
		
		if(bookAuthor == null) {
			throw new BookAuthorNotFoundException(id);
		}
		
		objects.remove(bookAuthor);
	}

	private void validateBookAuthor(BookAuthor bookAuthor) {
		if(bookAuthor == null) {
			throw new BookAuthorCannotBeNullException();
		}
		
		if(bookAuthor.authorId == null){
			throw new InvalidBookAuthorAuthorIdException(bookAuthor.id, bookAuthor.authorId);
		}
		
		if(bookAuthor.authorId < 1) {
			throw new InvalidBookAuthorAuthorIdException(bookAuthor.id, bookAuthor.authorId);
		}
		
		if(bookAuthor.bookId == null){
			throw new InvalidBookAuthorBookIdException(bookAuthor.id, bookAuthor.bookId);
		}
		
		if(bookAuthor.bookId < 1) {
			throw new InvalidBookAuthorBookIdException(bookAuthor.id, bookAuthor.bookId);
		}
	}
	
	public List<BookAuthor> getBooksByAuthorId(Integer authorId) {
		List<BookAuthor> books = objects.stream().filter(ba->ba.authorId.equals(authorId)).collect(Collectors.toList());
		return books;
	}
	
	public List<BookAuthor> getBooksByBookId(Integer bookId) {
		List<BookAuthor> books = objects.stream().filter(ba->ba.bookId.equals(bookId)).collect(Collectors.toList());
		return books;
	}

	public BookAuthor getBookAuthor(Predicate<BookAuthor> predicate) {
		BookAuthor bookAuthor = objects.stream().filter(predicate).findFirst().orElse(null);
		return bookAuthor;
	}
}
