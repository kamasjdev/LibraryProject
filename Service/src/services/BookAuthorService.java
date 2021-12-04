package services;

import java.util.List;

import entities.BookAuthor;
import exceptions.service.bookauthor.BookAuthorCannotBeNullException;
import exceptions.service.bookauthor.BookAuthorNotFoundException;
import exceptions.service.bookauthor.InvalidBookAuthorAuthorIdException;
import exceptions.service.bookauthor.InvalidBookAuthorBookIdException;
import interfaces.BaseService;
import interfaces.BookAuthorRepository;

public class BookAuthorService implements BaseService<BookAuthor> {
	private final BookAuthorRepository bookAuthorRepository;
	
	public BookAuthorService(BookAuthorRepository bookAuthorRepository) {
		this.bookAuthorRepository = bookAuthorRepository;
	}
	
	@Override
	public BookAuthor getById(Integer id) {
		BookAuthor bookAuthor = bookAuthorRepository.get(id);
		return bookAuthor;
	}

	@Override
	public List<BookAuthor> getEntities() {
		List<BookAuthor> bookAuthors = bookAuthorRepository.getAll();
		return bookAuthors;
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
		bookAuthorRepository.update(bookAuthor);
	}

	@Override
	public Integer add(BookAuthor entity) {
		validateBookAuthor(entity);
		
		Integer id = bookAuthorRepository.add(entity);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		BookAuthor bookAuthor = getById(id);
		
		if(bookAuthor == null) {
			throw new BookAuthorNotFoundException(id);
		}
		
		bookAuthorRepository.delete(bookAuthor);
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
		List<BookAuthor> books = bookAuthorRepository.getByAuthorId(authorId);
		return books;
	}
	
	public List<BookAuthor> getBooksByBookId(Integer bookId) {
		List<BookAuthor> books = bookAuthorRepository.getByBookId(bookId);
		return books;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
