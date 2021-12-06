package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import entities.Author;
import exceptions.repository.author.AuthorCannotBeNullException;
import exceptions.repository.author.AuthorIdCannotBeNullException;
import interfaces.AuthorRepository;

public class AuthorRepositoryImpl implements AuthorRepository {
	private final SessionFactory sessionFactory;
	
	public AuthorRepositoryImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Integer add(Author entity) {
		if(entity == null) {
			throw new AuthorCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.persist(entity);  
	    transaction.commit();    
	    session.close();    
		
		return entity.id;
	}

	public void delete(Author entity) {
		if(entity == null) {
			throw new AuthorCannotBeNullException();
		}
		
		delete(entity.id);
	}
	

	public void delete(Integer id) {
		if(id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Author author = (Author) session.get(Author.class, id);
		
		if(author != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(author);
			transaction.commit();
		}
		
	    session.close();    
	}

	public void update(Author entity) {
		if(entity.id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.merge(entity);  
	    transaction.commit();    
	    session.close();    
	}

	public Author get(Integer id) {
		if(id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Author author = session.get(Author.class, id);
	    session.close();    
		
		return author;
	}

	public List<Author> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Author a ");		
		List<Author> authors = query.getResultList();    
	    session.close();
		return authors;
	}

	public Author getAuthorDetails(Integer id) {
		if(id == null) {
			throw new AuthorIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT a " +
				"FROM Author as a " +
				"LEFT JOIN FETCH a.books as ba " +
				"LEFT JOIN FETCH ba.book as b " +
				"WHERE a.id = :id");
		query.setParameter("id", id);
		Author author = (Author) query.getResultList().get(0);		
	    session.close(); 
		
		return author;
	}

	public int getCount() {		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		@SuppressWarnings("rawtypes")
		Query query = session.createQuery(
				"SELECT COUNT(a.id) FROM Author a ");
		int count = ((Long) query.uniqueResult()).intValue();
		transaction.commit();
	    session.close();
		return count;
	}
	
}
