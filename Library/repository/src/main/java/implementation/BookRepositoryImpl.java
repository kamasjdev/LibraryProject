package implementation;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entities.Book;
import repository.BookRepository;

@Repository
public class BookRepositoryImpl implements BookRepository {

	private static final Logger logger = LoggerFactory.getLogger(BookRepositoryImpl.class);
	private final EntityManager entityManager;
	
	@Autowired
	public BookRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Integer add(Book book) {
		logger.info(String.format("Adding entity %1$s", Book.class.getName()));
        entityManager.persist(book);
		return book.id;
	}

	@Override
	public void delete(Integer id) {
		logger.info(String.format("Deleting entity %1$s", Book.class.getName()));
		final Book book = get(id);
        delete(book);
	}

	@Override
	public void delete(Book book) {
		logger.info(String.format("Deleting entity %1$s", Book.class.getName()));
        entityManager.remove(book);		
	}

	@Override
	public void update(Book book) {
		logger.info(String.format("Updating entity %1$s", Book.class.getName()));
        entityManager.merge(book);
	}

	@Override
	public Book get(Integer id) {
		logger.info(String.format("Getting entity %1$s", Book.class.getName()));
		Book book = entityManager.find(Book.class, id);
		return book;
	}

	@Override
	public List<Book> getAll() {
		logger.info(String.format("Getting all entities %1$s", Book.class.getName()));
		List<Book> books = entityManager.createQuery("from Book").getResultList();
		return books;
	}
}
