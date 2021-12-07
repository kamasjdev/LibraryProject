package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entities.BookAuthor;
import exceptions.repository.bookauthor.BookAuthorCannotBeNullException;
import exceptions.repository.bookauthor.BookAuthorIdCannotBeNullException;
import interfaces.BookAuthorRepository;

public class BookAuthorRepositoryImpl implements BookAuthorRepository {
private final SessionFactory sessionFactory;
	
	public BookAuthorRepositoryImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Integer add(BookAuthor entity) {
		if(entity == null) {
			throw new BookAuthorCannotBeNullException();
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
			throw new BookAuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		BookAuthor bookAuthor = (BookAuthor) session.get(BookAuthor.class,id);
		
		if(bookAuthor != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(bookAuthor);
			transaction.commit();
		}
		
	    session.close();   
	}

	public void delete(BookAuthor entity) {
		if(entity == null) {
			throw new BookAuthorCannotBeNullException();
		}
		
		delete(entity.id);
	}

	public void update(BookAuthor entity) {
		if(entity == null) {
			throw new BookAuthorCannotBeNullException();
		}
		
		if(entity.id == null) {
			throw new BookAuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.merge(entity);  
	    transaction.commit();    
	    session.close(); 
	}

	public BookAuthor get(Integer id) {
		if(id == null) {
			throw new BookAuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		BookAuthor author = session.get(BookAuthor.class, id);
	    session.close();    
		
		return author;
	}

	public List<BookAuthor> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM BookAuthor ba ");		
		List<BookAuthor> bookAuthors = query.getResultList();    
	    session.close();		
		return bookAuthors;
	}

	public List<BookAuthor> getByAuthorId(Integer authorId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT ba " +
				"FROM BookAuthor ba " +
				"JOIN ba.author a " +
				"WHERE a.id = :id");
		query.setParameter("id", authorId);
		List<BookAuthor> bookAuthors = query.getResultList();
	    session.close(); 
		
		return bookAuthors;
	}

	public List<BookAuthor> getByBookId(Integer bookId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT ba " +
				"FROM BookAuthor ba " +
				"JOIN ba.book b " +
				"WHERE b.id = :id");
		query.setParameter("id", bookId);
		List<BookAuthor> bookAuthors = query.getResultList();
	    session.close(); 
	    
	    return bookAuthors;
	}
	
	public int getCount() {
		Session session = sessionFactory.openSession();
		@SuppressWarnings("rawtypes")
		Query query = session.createQuery(
				"SELECT COUNT(ba.id) FROM BookAuthor ba ");
		int count = ((Long) query.uniqueResult()).intValue();
	    session.close();
		return count;
	}
}
