package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entities.Book;
import entities.BookCustomer;
import exceptions.repository.bookcustomer.BookCustomerCannotBeNullException;
import exceptions.repository.bookcustomer.BookCustomerIdCannotBeNullException;
import interfaces.BookCustomerRepository;

public class BookCustomerRepositoryImpl implements BookCustomerRepository {
private final SessionFactory sessionFactory;
	
	public BookCustomerRepositoryImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Integer add(BookCustomer entity) {
		if(entity == null) {
			throw new BookCustomerCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.persist(entity);  
	    transaction.commit();    
	    session.close();    
		
		return entity.id;
	}

	public void delete(Integer id) {
		if(id == null) {
			throw new BookCustomerIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		BookCustomer bookCustomer = (BookCustomer) session.get(BookCustomer.class,id);
		
		if(bookCustomer != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(bookCustomer);
			transaction.commit();
		}
		
	    session.close();    
	}

	public void delete(BookCustomer entity) {
		if(entity == null) {
			throw new BookCustomerCannotBeNullException();
		}

		delete(entity.id);
	}

	public void update(BookCustomer entity) {
		if(entity == null) {
			throw new BookCustomerCannotBeNullException();
		}
		
		if(entity.id == null) {
			throw new BookCustomerIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.merge(entity);  
	    transaction.commit();    
	    session.close();    
	}

	public BookCustomer get(Integer id) {
		if(id == null) {
			throw new BookCustomerIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		BookCustomer bookCustomer = session.get(BookCustomer.class, id);
	    session.close();    
		
		return bookCustomer;
	}

	public List<BookCustomer> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM BookCustomer bc ");		
		List<BookCustomer> authors = query.getResultList();    
	    session.close();		
		return authors;
	}

	public BookCustomer getBookCustomerByBookIdAndCustomerId(Integer bookId, Integer customerId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT bc " +
				"FROM BookCustomer as bc " +
				"WHERE bc.customerId = :customerId AND bc.bookId = :bookId");
		query.setParameter("bookId", bookId);
		query.setParameter("customerId", customerId);
		BookCustomer bookCustomer = (BookCustomer) query.getResultList().get(0);
	    session.close(); 
	    
	    return bookCustomer;
	}

	@Override
	public int getCount() {
		Session session = sessionFactory.openSession();
		@SuppressWarnings("rawtypes")
		Query query = session.createQuery(
				"SELECT COUNT(bc.id) FROM BookCustomer bc ");
		int count = ((Long) query.uniqueResult()).intValue();
	    session.close();
		return count;
	}
}
