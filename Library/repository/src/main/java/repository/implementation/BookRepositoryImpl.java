package repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entities.Book;
import repository.BookRepository;

@Repository
public class BookRepositoryImpl implements BookRepository {

	private static final Logger logger = LoggerFactory.getLogger(BookRepositoryImpl.class);
	
	@PersistenceContext
    private EntityManager entityManager;
	
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
	
	public void deleteBookWithBookAuthors(int bookId) {
		logger.info(String.format("Deleting entity %1$s", Book.class.getName()));
		Query queryBookAuthors = entityManager.createQuery("DELETE FROM BookAuthor WHERE bookId = :bookId");
		queryBookAuthors.setParameter("bookId", bookId);
		queryBookAuthors.executeUpdate();
		Query queryBook = entityManager.createQuery("DELETE FROM Book WHERE id = :bookId");
		queryBook.setParameter("bookId", bookId);
		queryBook.executeUpdate();
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

	@Override
	public Book getBookDetails(int bookId) {
		logger.info(String.format("Getting book details with id: %1$s", bookId));
		Query query = entityManager.createQuery(
				"SELECT b " +
				"FROM Book as b " +
				"JOIN b.authors as ba " +
				"JOIN ba.author as a " +
				"LEFT JOIN FETCH b.customers as bc " +
				"LEFT JOIN FETCH bc.customer as c " +
				"WHERE b.id = :id");
		query.setParameter("id", bookId);
		List books = query.getResultList();
		
		if(books.isEmpty()) {
			return null;
		}
		
		Book book = (Book) query.getResultList().get(0);
		return book;
	}

	@Override
	public Book getBookWithoutAuthors(int bookId) {
		logger.info(String.format("Getting book without authors with id: %1$s", bookId));
		Query query = entityManager.createQuery(
				"SELECT b " +
				"FROM Book as b " +
				"WHERE b.id = :id");
		query.setParameter("id", bookId);
		Book book = (Book) query.getResultList().get(0);
		return book;
	}
}
