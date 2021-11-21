package services;

import java.util.List;

import abstracts.AbstractBaseService;
import entities.Author;
import exceptions.service.author.AuthorCannotBeNullException;
import exceptions.service.author.AuthorFirstNameCannotBeEmptyException;
import exceptions.service.author.AuthorLastNameCannotBeEmptyException;
import exceptions.service.author.AuthorNotFoundException;

public class AuthorService extends AbstractBaseService<Author> {
	
	@Override
	public Author getById(Integer id) {
		Author author = objects.stream().filter(o->o.id.equals(id)).findFirst().orElse(null);
		return author;
	}

	@Override
	public List<Author> getEntities() {
		return objects;
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
		
	}

	@Override
	public Integer add(Author entity) {
		validateAuthor(entity);
		
		Integer id = getLastId();
		entity.id = id;
		objects.add(entity);
		
		return id;
	}

	@Override
	public void delete(Integer id) {
		Author author = getById(id);
		
		if(author == null) {
			throw new AuthorNotFoundException(id);
		}
		
		objects.remove(author);
	}

	private void validateAuthor(Author author) {
		if(author == null)
		{
			throw new AuthorCannotBeNullException();
		}
		
		if(author.person.firstName.isEmpty())
		{
			throw new AuthorFirstNameCannotBeEmptyException(author.id);
		}
		
		if(author.person.lastName.isEmpty())
		{
			throw new AuthorLastNameCannotBeEmptyException(author.id);
		}
	}
}
