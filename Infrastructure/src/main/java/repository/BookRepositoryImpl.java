package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entities.Book;
import exceptions.repository.book.BookCannotBeNullException;
import exceptions.repository.book.BookIdCannotBeNullException;
import exceptions.repository.bookauthor.BookAuthorsCannotBeEmptyOrNullException;
import interfaces.BookRepository;

public class BookRepositoryImpl implements BookRepository {
	private final SessionFactory sessionFactory;
	
	public BookRepositoryImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Integer add(Book entity) {
		validateBook(entity);
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.persist(entity);  
	    transaction.commit();    
	    session.close();    
		
		return entity.id;
	}

	public void delete(Integer id) {
		if(id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Book book = (Book) session.get(Book.class, id);
		
		if(book != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(book);
			transaction.commit();
		}
		
	    session.close();    
	}

	public void delete(Book entity) {
		if(entity == null) {
			throw new BookCannotBeNullException();
		}
		
		delete(entity.id);
	}

	public void update(Book entity) {
		validateBook(entity);
		
		if(entity.id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.merge(entity);  
	    transaction.commit();    
	    session.close();  
	}
	
	public Book get(Integer id) {
		if(id == null) {
			throw new BookIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Book book = session.get(Book.class, id);
		  
	    session.close();    
		
		return book;
	}

	public List<Book> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT b FROM Book b ");		
		List<Book> books = query.getResultList();    
	    session.close();		
		return books;
	}
		
	public Book getBookDetails(Integer id) {
		if(id == null) {
			throw new BookIdCannotBeNullException();
		}
	
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT b " +
				"FROM Book as b " +
				"JOIN b.authors as ba " +
				"JOIN ba.author as a " +
				"LEFT JOIN FETCH b.customers as bc " +
				"LEFT JOIN FETCH bc.customer as c " +
				"WHERE b.id = :id");
		query.setParameter("id", id);
		List books = query.getResultList();
		
		if(books.isEmpty()) {
			return null;
		}
		
		Book book = (Book) query.getResultList().get(0);
	    session.close(); 
		
		return book;
	}
	
	private void validateBook(Book book) {
		if(book == null) {
			throw new BookCannotBeNullException();
		}
		
		if(book.authors == null) {
			throw new BookAuthorsCannotBeEmptyOrNullException();
		}
		
		if(book.authors.isEmpty()) {
			throw new BookAuthorsCannotBeEmptyOrNullException();
		}
	}

	public int getCount() {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		@SuppressWarnings("rawtypes")
		Query query = session.createQuery(
				"SELECT COUNT(b.id) FROM Book a ");
		int count = ((Long) query.uniqueResult()).intValue();
		transaction.commit();    
	    session.close();
		return count;
	}

	public Book getBookWithoutAuthors(Integer id) {
		if(id == null) {
			throw new BookIdCannotBeNullException();
		}
				
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT b " +
				"FROM Book as b " +
				"WHERE b.id = :id");
		query.setParameter("id", id);
		Book book = (Book) query.getResultList().get(0);
	    session.close(); 
		
		return book;
	}
}
