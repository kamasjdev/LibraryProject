package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import entities.Bill;
import exceptions.repository.bill.BillCannotBeNullException;
import exceptions.repository.bill.BillIdCannotBeNullException;
import exceptions.repository.bill.CustomerIdForBillCannotBeNullException;
import interfaces.BillRepository;

public class BillRepositoryImpl extends BaseRepository implements BillRepository {
	private final String fileName;
	private final SessionFactory sessionFactory;
	
	public BillRepositoryImpl(String fileName) {
		this.fileName = fileName;
		sessionFactory = new Configuration().configure(fileName).buildSessionFactory();
	}
	
	public Integer add(Bill entity) {
		if(entity == null) {
			throw new BillCannotBeNullException();
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
			throw new BillIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Bill author = (Bill) session.load(Bill.class,id);
		session.delete(author);
		transaction.commit();
		sessionFactory.close();  
	    session.close();    
	}

	public void delete(Bill entity) {
		if(entity == null) {
			throw new BillCannotBeNullException();
		}
		
		delete(entity.id);
	}

	public void update(Bill entity) {
		if(entity == null) {
			throw new BillCannotBeNullException();
		}
		
		if(entity.id == null) {
			throw new BillIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.merge(entity);  
	    transaction.commit();    
	    sessionFactory.close();  
	    session.close();    
	}

	public Bill get(Integer id) {
		if(id == null) {
			throw new BillIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Bill bill = session.get(Bill.class, id);
		transaction.commit();    
		sessionFactory.close();  
	    session.close();    
		
		return bill;
	}

	public List<Bill> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Author a ");		
		List<Bill> authors = query.getResultList();    
		sessionFactory.close();  
	    session.close();		
		return authors;
	}

	public void deleteAllBillsByCustomerId(Integer customerId) {
		if(customerId == null) {
			throw new CustomerIdForBillCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery(
				"DELETE FROM Bill as b WHERE b.customerId = :customerId");
		query.setParameter("customerId", customerId);
		query.executeUpdate();
		transaction.commit();
		sessionFactory.close();  
	    session.close(); 
	}

}