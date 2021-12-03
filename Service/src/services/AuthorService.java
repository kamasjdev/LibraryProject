package services;

import java.util.List;

import entities.Author;
import exceptions.service.author.AuthorCannotBeNullException;
import exceptions.service.author.AuthorFirstNameCannotBeEmptyException;
import exceptions.service.author.AuthorLastNameCannotBeEmptyException;
import exceptions.service.author.AuthorNotFoundException;
import interfaces.AuthorRepository;
import interfaces.BaseService;

public class AuthorService implements BaseService<Author> {
	private final AuthorRepository authorRepository;
	
	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}
	
	@Override
	public Author getById(Integer id) {
		Author author = authorRepository.get(id);
		return author;
	}

	@Override
	public List<Author> getEntities() {
		List<Author> authors = authorRepository.getAll();
		return authors;
	}

	@Override
	public void update(Author entity) {
		validateAuthor(entity);
		Author author = getById(entity.id);
		
		if(author == null) {
			throw new AuthorNotFoundException(entity.id);
		}
		
		author.person.firstName = entity.person.firstName;
		author.person.lastName = entity.person.lastName;
		authorRepository.update(author);
	}

	@Override
	public Integer add(Author entity) {
		validateAuthor(entity);
		
		Integer id = authorRepository.add(entity);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		Author author = getById(id);
		
		if(author == null) {
			throw new AuthorNotFoundException(id);
		}
		
		authorRepository.delete(author);
	}

	private void validateAuthor(Author author) {
		if(author == null)
		{
			throw new AuthorCannotBeNullException();
		}
		
		if(author.person.firstName == null)
		{
			throw new AuthorFirstNameCannotBeEmptyException(author.id);
		}
		
		if(author.person.firstName.isEmpty())
		{
			throw new AuthorFirstNameCannotBeEmptyException(author.id);
		}
		
		if(author.person.lastName == null)
		{
			throw new AuthorLastNameCannotBeEmptyException(author.id);
		}
		
		if(author.person.lastName.isEmpty())
		{
			throw new AuthorLastNameCannotBeEmptyException(author.id);
		}
	}
}
