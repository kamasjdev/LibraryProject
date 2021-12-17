package implementation;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entities.BookAuthor;
import repository.BookAuthorRepository;

@Repository
public class BookAuthorRepositoryImpl implements BookAuthorRepository {

	private static final Logger logger = LoggerFactory.getLogger(BookAuthorRepositoryImpl.class);
	private final EntityManager entityManager;
	
	@Autowired
	public BookAuthorRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Integer add(BookAuthor bookAuthor) {
		logger.info(String.format("Adding entity %1$s", BookAuthor.class.getName()));
        entityManager.persist(bookAuthor);
		return bookAuthor.id;
	}

	@Override
	public void delete(Integer id) {
		logger.info(String.format("Deleting entity %1$s", BookAuthor.class.getName()));
		final BookAuthor bookAuthor = get(id);
        delete(bookAuthor);
	}

	@Override
	public void delete(BookAuthor bookAuthor) {
		logger.info(String.format("Deleting entity %1$s", BookAuthor.class.getName()));
        entityManager.remove(bookAuthor);		
	}

	@Override
	public void update(BookAuthor bookAuthor) {
		logger.info(String.format("Updating entity %1$s", BookAuthor.class.getName()));
        entityManager.merge(bookAuthor);
	}

	@Override
	public BookAuthor get(Integer id) {
		logger.info(String.format("Getting entity %1$s", BookAuthor.class.getName()));
		BookAuthor bookAuthor = entityManager.find(BookAuthor.class, id);
		return bookAuthor;
	}

	@Override
	public List<BookAuthor> getAll() {
		logger.info(String.format("Getting all entities %1$s", BookAuthor.class.getName()));
		List<BookAuthor> bookAuthors = entityManager.createQuery("from Author").getResultList();
		return bookAuthors;
	}

}
