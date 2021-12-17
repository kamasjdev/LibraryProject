package services;

import java.util.ArrayList;
import java.util.List;

import dto.BookAuthorDto;
import entities.BookAuthor;
import exceptions.service.bookauthor.BookAuthorCannotBeNullException;
import exceptions.service.bookauthor.BookAuthorNotFoundException;
import exceptions.service.bookauthor.InvalidBookAuthorAuthorIdException;
import exceptions.service.bookauthor.InvalidBookAuthorBookIdException;
import helpers.services.mappings.Mapper;
import interfaces.BaseService;
import interfaces.BookAuthorRepository;
import interfaces.BookAuthorService;

public class BookAuthorServiceImpl implements BookAuthorService {
	private final BookAuthorRepository bookAuthorRepository;
	
	public BookAuthorServiceImpl(BookAuthorRepository bookAuthorRepository) {
		this.bookAuthorRepository = bookAuthorRepository;
	}
	
	@Override
	public BookAuthorDto getById(Integer id) {
		BookAuthor bookAuthor = bookAuthorRepository.get(id);
		
		if(bookAuthor == null) {
			return null;
		}
		
		BookAuthorDto bookAuthorDto = Mapper.mapToBookAuthorDto(bookAuthor);
		return bookAuthorDto;
	}

	@Override
	public List<BookAuthorDto> getAll() {
		List<BookAuthor> bookAuthors = bookAuthorRepository.getAll();
		List<BookAuthorDto> bookAuthorsDto = new ArrayList<BookAuthorDto>();
		
		for(BookAuthor bookAuthor : bookAuthors) {
			BookAuthorDto bookAuthorDto = Mapper.mapToBookAuthorDto(bookAuthor);
			bookAuthorsDto.add(bookAuthorDto);
		}
		
		return bookAuthorsDto;
	}

	@Override
	public void update(BookAuthorDto dto) {
		validateBookAuthor(dto);
		
		BookAuthorDto bookAuthorDto = getById(dto.id);
		
		if(bookAuthorDto == null) {
			throw new BookAuthorNotFoundException(dto.id);
		}
		
		bookAuthorDto.authorId = dto.authorId;
		bookAuthorDto.bookId = dto.bookId;
		BookAuthor bookAuthor = Mapper.mapToBookAuthor(bookAuthorDto); 
		bookAuthorRepository.update(bookAuthor);
	}

	@Override
	public Integer add(BookAuthorDto dto) {
		validateBookAuthor(dto);
		BookAuthor bookAuthor = BookAuthor.create(dto.bookId, dto.authorId);
		
		Integer id = bookAuthorRepository.add(bookAuthor);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		BookAuthorDto bookAuthorDto = getById(id);
		
		if(bookAuthorDto == null) {
			throw new BookAuthorNotFoundException(id);
		}
		
		BookAuthor bookAuthor = Mapper.mapToBookAuthor(bookAuthorDto);
		bookAuthorRepository.delete(bookAuthor);
	}

	private void validateBookAuthor(BookAuthorDto bookAuthorDto) {
		if(bookAuthorDto == null) {
			throw new BookAuthorCannotBeNullException();
		}
		
		if(bookAuthorDto.authorId == null){
			throw new InvalidBookAuthorAuthorIdException(bookAuthorDto.id, bookAuthorDto.authorId);
		}
		
		if(bookAuthorDto.authorId < 1) {
			throw new InvalidBookAuthorAuthorIdException(bookAuthorDto.id, bookAuthorDto.authorId);
		}
		
		if(bookAuthorDto.bookId == null){
			throw new InvalidBookAuthorBookIdException(bookAuthorDto.id, bookAuthorDto.bookId);
		}
		
		if(bookAuthorDto.bookId < 1) {
			throw new InvalidBookAuthorBookIdException(bookAuthorDto.id, bookAuthorDto.bookId);
		}
	}
	
	public List<BookAuthor> getBooksByAuthorId(Integer authorId) {
		List<BookAuthor> books = bookAuthorRepository.getByAuthorId(authorId);
		return books;
	}
	
	public List<BookAuthorDto> getBooksByBookId(Integer bookId) {
		List<BookAuthor> books = bookAuthorRepository.getByBookId(bookId);
		List<BookAuthorDto> bookAuthorsDto = new ArrayList<BookAuthorDto>();
		
		for(BookAuthor bookAuthor : books) {
			BookAuthorDto bookAuthorDto = Mapper.mapToBookAuthorDto(bookAuthor);
			bookAuthorsDto.add(bookAuthorDto);
		}
		
		return bookAuthorsDto;
	}
}