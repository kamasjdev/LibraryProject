package repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entities.Author;
import repository.AuthorRepository;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

	private static final Logger logger = LoggerFactory.getLogger(AuthorRepositoryImpl.class);
	private final EntityManager entityManager;
	
	@Autowired
	public AuthorRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Integer add(Author author) {
		logger.info(String.format("Adding entity %1$s", Author.class.getName()));
        entityManager.persist(author);
		return author.id;
	}

	@Override
	public void delete(Integer id) {
		logger.info(String.format("Deleting entity %1$s", Author.class.getName()));
		final Author author = get(id);
        delete(author);
	}

	@Override
	public void delete(Author author) {
		logger.info(String.format("Deleting entity %1$s", Author.class.getName()));
        entityManager.remove(author);		
	}

	@Override
	public void update(Author author) {
		logger.info(String.format("Updating entity %1$s", Author.class.getName()));
        entityManager.merge(author);
	}

	@Override
	public Author get(Integer id) {
		logger.info(String.format("Getting entity %1$s", Author.class.getName()));
		Author author = entityManager.find(Author.class, id);
		return author;
	}

	@Override
	public List<Author> getAll() {
		logger.info(String.format("Getting all entities %1$s", Author.class.getName()));
		List<Author> authors = entityManager.createQuery("from Author").getResultList();
		return authors;
	}

	@Override
	public Author getAuthorDetails(int authorId) {
		Query query = entityManager.createQuery(
				"SELECT a " +
				"FROM Author as a " +
				"LEFT JOIN FETCH a.books as ba " +
				"LEFT JOIN FETCH ba.book as b " +
				"WHERE a.id = :id");
		query.setParameter("id", authorId);
		List resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		}
		
		Author author = (Author) resultList.get(0);	
		return author;
	}

}
