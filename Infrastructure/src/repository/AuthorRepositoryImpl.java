package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.Author;
import exceptions.repository.author.AuthorCannotBeNullException;
import exceptions.repository.author.AuthorIdCannotBeNullException;
import interfaces.AuthorRepository;
import interfaces.DbClient;
import interfaces.MapEntity;

public class AuthorRepositoryImpl implements AuthorRepository {
	private DbClient dbClient;
	private MapEntity<Author> mapper;
	
	public AuthorRepositoryImpl(DbClient dbClient, MapEntity<Author> mapper) {
		this.dbClient = dbClient;
		this.mapper = mapper;
	}
	
	@Override
	public Integer add(Author entity) {
		Integer id = dbClient.insert("INSERT INTO AUTHORS(FIRST_NAME,LAST_NAME) VALUES(?, ?)", entity.person.firstName, entity.person.lastName);
		return id;
	}

	@Override
	public void delete(Author entity) {
		if(entity == null) {
			throw new AuthorCannotBeNullException();
		}
		
		delete(entity.id);
	}
	

	@Override
	public void delete(Integer id) {
		if(id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		dbClient.delete("DELETE FROM AUTHORS WHERE id = ?", id);
	}

	@Override
	public void update(Author entity) {
		if(entity.id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		dbClient.update("UPDATE AUTHORS SET FIRST_NAME = ?, LAST_NAME = ? WHERE id = ?", entity.person.firstName, entity.person.lastName, entity.id);
	}

	@Override
	public Author get(Integer id) {
		List<List<Map<String, Object>>> authors = dbClient.executeQuery("SELECT * FROM AUTHORS WHERE id = ?", id);
		Author author = null;
		
		if(authors.size() > 0) {
			List<Map<String, Object>> authorsFields = authors.get(0);
			author = mapper.Map(authorsFields);
		}
		
		return author;
	}

	@Override
	public List<Author> getAll() {
		List<Author> authors = new ArrayList<Author>();
		List<List<Map<String, Object>>> authorsFromDb = dbClient.executeQuery("SELECT * FROM AUTHORS");

		for(List<Map<String,Object>> author : authorsFromDb) {
			authors.add(mapper.Map(author));
		}
		
		return authors;
	}
	
}
