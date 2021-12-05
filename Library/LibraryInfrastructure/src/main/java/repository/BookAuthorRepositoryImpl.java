package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import entities.Author;
import entities.Book;
import entities.BookAuthor;
import exceptions.repository.bookauthor.BookAuthorCannotBeNullException;
import exceptions.repository.bookauthor.BookAuthorIdCannotBeNullException;
import interfaces.BookAuthorRepository;

public class BookAuthorRepositoryImpl extends BaseRepository implements BookAuthorRepository {
	private final String fileName;
	private final SessionFactory sessionFactory;
	
	public BookAuthorRepositoryImpl(String fileName) {
		this.fileName = fileName;
		sessionFactory = new Configuration().configure(fileName).buildSessionFactory();
	}
	
	public Integer add(BookAuthor entity) {
		if(entity == null) {
			throw new BookAuthorCannotBeNullException();
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
			throw new BookAuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		BookAuthor author = (BookAuthor) session.load(BookAuthor.class,id);
		session.delete(author);
		transaction.commit();
		sessionFactory.close();  
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
	    sessionFactory.close();  
	    session.close(); 
	}

	public BookAuthor get(Integer id) {
		if(id == null) {
			throw new BookAuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		BookAuthor author = session.get(BookAuthor.class, id);
		transaction.commit();    
		sessionFactory.close();  
	    session.close();    
		
		return author;
	}

	public List<BookAuthor> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM BookAuthor ba ");		
		List<BookAuthor> bookAuthors = query.getResultList();    
		sessionFactory.close();  
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
		sessionFactory.close();  
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
		sessionFactory.close();  
	    session.close(); 
	    
	    return bookAuthors;
	}
}
