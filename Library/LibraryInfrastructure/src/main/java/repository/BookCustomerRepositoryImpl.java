package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import entities.Book;
import entities.BookCustomer;
import exceptions.repository.bookcustomer.BookCustomerCannotBeNullException;
import exceptions.repository.bookcustomer.BookCustomerIdCannotBeNullException;
import interfaces.BookCustomerRepository;

public class BookCustomerRepositoryImpl extends BaseRepository implements BookCustomerRepository {
	private final String fileName;
	private final SessionFactory sessionFactory;
	
	public BookCustomerRepositoryImpl(String fileName) {
		this.fileName = fileName;
		sessionFactory = new Configuration().configure(fileName).buildSessionFactory();
	}
	
	public Integer add(BookCustomer entity) {
		if(entity == null) {
			throw new BookCustomerCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.persist(entity);  
	    transaction.commit();    
	    sessionFactory.close();  
	    session.close();    
		
		return entity.id;
	}

	public void delete(Integer id) {
		if(id == null) {
			throw new BookCustomerIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Book author = (Book) session.load(Book.class,id);
		session.delete(author);
		transaction.commit();
		sessionFactory.close();  
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
	    sessionFactory.close();  
	    session.close();    
	}

	public BookCustomer get(Integer id) {
		if(id == null) {
			throw new BookCustomerIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		BookCustomer bookCustomer = session.get(BookCustomer.class, id);
		transaction.commit();    
		sessionFactory.close();  
	    session.close();    
		
		return bookCustomer;
	}

	public List<BookCustomer> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM BookCustomer bc ");		
		List<BookCustomer> authors = query.getResultList();    
		sessionFactory.close();  
	    session.close();		
		return authors;
	}

	public BookCustomer getBookCustomerByBookIdAndCustomerId(Integer bookId, Integer customerId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT bc " +
				"FROM BookCustomer bc " +
				"JOIN b.book b " +
				"JOIN ba.customers c " +
				"WHERE c.id = :customerId AND b.id = :bookId");
		query.setParameter("bookId", bookId);
		query.setParameter("customerId", customerId);
		BookCustomer bookCustomer = (BookCustomer) query.getResultList().get(0);
		sessionFactory.close();  
	    session.close(); 
	    
	    return bookCustomer;
	}
}
