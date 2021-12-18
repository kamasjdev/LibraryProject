package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dto.AuthorDto;
import dto.BookAuthorDto;
import dto.BookDto;
import entities.Author;
import entities.BookAuthor;
import exceptions.service.author.AuthorCannotBeNullException;
import exceptions.service.author.AuthorFirstNameCannotBeEmptyException;
import exceptions.service.author.AuthorLastNameCannotBeEmptyException;
import exceptions.service.author.AuthorNotFoundException;
import helpers.services.mappings.Mapper;
import interfaces.AuthorService;
import repository.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {
	private final AuthorRepository authorRepository;
	
	@Autowired
	public AuthorServiceImpl(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}
	
	@Override
	public AuthorDto getById(Integer id) {
		Author author = authorRepository.get(id);
		
		if(author == null) {
			return null;
		}
		
		AuthorDto authorDto = Mapper.mapToAuthorDto(author);
		return authorDto;
	}

	@Override
	public List<AuthorDto> getAll() {
		List<Author> authors = authorRepository.getAll();
		List<AuthorDto> authorsDto = new ArrayList<AuthorDto>();
		
		for(Author author : authors) {
			AuthorDto authorDto = Mapper.mapToAuthorDto(author);
			authorsDto.add(authorDto);
		}
			
		return authorsDto;
	}
	
	@Override
	public void update(AuthorDto dto) {
		validateAuthor(dto);
		AuthorDto authorDto = getById(dto.id);
		
		if(authorDto == null) {
			throw new AuthorNotFoundException(dto.id);
		}
		
		authorDto.firstName = dto.firstName;
		authorDto.lastName = dto.lastName;
		Author author = Mapper.mapToAuthor(authorDto);
		
		authorRepository.update(author);
	}

	@Override
	public Integer add(AuthorDto dto) {
		validateAuthor(dto);
		Author author = Author.create(dto.firstName, dto.lastName);
		
		Integer id = authorRepository.add(author);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		AuthorDto authorDto = getById(id);
		
		if(authorDto == null) {
			throw new AuthorNotFoundException(id);
		}
		
		authorRepository.delete(id);
	}

	private void validateAuthor(AuthorDto authorDto) {
		if(authorDto == null)
		{
			throw new AuthorCannotBeNullException();
		}
		
		if(authorDto.firstName == null)
		{
			throw new AuthorFirstNameCannotBeEmptyException(authorDto.id);
		}
		
		if(authorDto.firstName.isEmpty())
		{
			throw new AuthorFirstNameCannotBeEmptyException(authorDto.id);
		}
		
		if(authorDto.lastName == null)
		{
			throw new AuthorLastNameCannotBeEmptyException(authorDto.id);
		}
		
		if(authorDto.lastName.isEmpty())
		{
			throw new AuthorLastNameCannotBeEmptyException(authorDto.id);
		}
	}

	public AuthorDto getDetails(Integer id) {
		Author author = authorRepository.getAuthorDetails(id);
		
		if(author == null) {
			return null;
		}
		
		AuthorDto authorDto = Mapper.mapToAuthorDto(author);
		
		for(BookAuthor bookAuthor : author.books) {
			BookAuthorDto bookAuthorDto = Mapper.mapToBookAuthorDto(bookAuthor);
			BookDto book = Mapper.mapToBookDto(bookAuthor.book);
			bookAuthorDto.book = book;
			authorDto.books.add(bookAuthorDto);
		}
		
		return authorDto;
	}
}
