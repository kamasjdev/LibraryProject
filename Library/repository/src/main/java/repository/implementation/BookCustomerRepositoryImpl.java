package repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entities.BookCustomer;
import repository.BookCustomerRepository;

@Repository
public class BookCustomerRepositoryImpl implements BookCustomerRepository {

	private static final Logger logger = LoggerFactory.getLogger(BookCustomerRepositoryImpl.class);
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Integer add(BookCustomer bookCustomer) {
		logger.info(String.format("Adding entity %1$s", BookCustomer.class.getName()));
        entityManager.persist(bookCustomer);
		return bookCustomer.id;
	}

	@Override
	public void delete(Integer id) {
		logger.info(String.format("Deleting entity %1$s", BookCustomer.class.getName()));
		final BookCustomer bookCustomer = get(id);
        delete(bookCustomer);
	}

	@Override
	public void delete(BookCustomer bookCustomer) {
		logger.info(String.format("Deleting entity %1$s", BookCustomer.class.getName()));
        entityManager.remove(bookCustomer);		
	}

	@Override
	public void update(BookCustomer bookCustomer) {
		logger.info(String.format("Updating entity %1$s", BookCustomer.class.getName()));
        entityManager.merge(bookCustomer);
	}

	@Override
	public BookCustomer get(Integer id) {
		logger.info(String.format("Getting entity %1$s", BookCustomer.class.getName()));
		BookCustomer author = entityManager.find(BookCustomer.class, id);
		return author;
	}

	@Override
	public List<BookCustomer> getAll() {
		logger.info(String.format("Getting all entities %1$s", BookCustomer.class.getName()));
		List<BookCustomer> bookCustomers = entityManager.createQuery("from BookCustomer").getResultList();
		return bookCustomers;
	}

	@Override
	public BookCustomer getBookCustomerByBookIdAndCustomerId(int bookId, int customerId) {
		logger.info(String.format("Getting book customer by book with id: %1$s and customer with id: %2$s", bookId, customerId));
		Query query = entityManager.createQuery(
				"SELECT bc " +
				"FROM BookCustomer as bc " +
				"WHERE bc.customerId = :customerId AND bc.bookId = :bookId");
		query.setParameter("bookId", bookId);
		query.setParameter("customerId", customerId);
		BookCustomer bookCustomer = (BookCustomer) query.getResultList().get(0);
		return bookCustomer;
	}

}
