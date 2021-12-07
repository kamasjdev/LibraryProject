package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entities.Customer;
import exceptions.repository.customer.CustomerCannotBeNullException;
import exceptions.repository.customer.CustomerIdCannotBeNullException;
import interfaces.CustomerRepository;

public class CustomerRepositoryImpl implements CustomerRepository {
	private final SessionFactory sessionFactory;
	
	public CustomerRepositoryImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Integer add(Customer entity) {
		if(entity == null) {
			throw new CustomerCannotBeNullException();
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
			throw new CustomerIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Customer customer = (Customer) session.get(Customer.class, id);
		
		if(customer != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(customer);
			transaction.commit();
		}
		
	    session.close();  
	}

	public void delete(Customer entity) {
		if(entity == null) {
			throw new CustomerCannotBeNullException();
		}
		
		delete(entity.id);
	}

	public void update(Customer entity) {
		if(entity == null) {
			throw new CustomerCannotBeNullException();
		}
		
		if(entity.id == null) {
			throw new CustomerIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.merge(entity);  
	    transaction.commit();    
	    session.close();    
	}

	public Customer get(Integer id) {
		if(id == null) {
			throw new CustomerIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Customer customer = session.get(Customer.class, id);
	    session.close();    
		
		return customer;
	}

	public List<Customer> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Customer c ");		
		List<Customer> customer = query.getResultList();
	    session.close();		
		return customer;
	}

	public int getCount() {
		Session session = sessionFactory.openSession();
		@SuppressWarnings("rawtypes")
		Query query = session.createQuery(
				"SELECT COUNT(c.id) FROM Customer c ");
		int count = ((Long) query.uniqueResult()).intValue();
	    session.close();
		return count;
	}

	public Customer getDetails(Integer customerId) {	
		Session session = sessionFactory.openSession();
		Query query = session.createQuery(
				"SELECT c " +
				"FROM Customer as c " +
				"LEFT JOIN FETCH c.books as bc " +
				"LEFT JOIN FETCH bc.book as b " +
				"LEFT JOIN FETCH c.bills as bill " +
				"WHERE c.id = :id");
		query.setParameter("id", customerId);
		Customer customer = (Customer) query.getResultList().get(0);
	    session.close(); 
		
		return customer;
	}
}
