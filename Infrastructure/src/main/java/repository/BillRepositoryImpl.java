package repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import entities.Bill;
import exceptions.repository.bill.BillCannotBeNullException;
import exceptions.repository.bill.BillIdCannotBeNullException;
import exceptions.repository.bill.CustomerIdForBillCannotBeNullException;
import interfaces.BillRepository;

public class BillRepositoryImpl implements BillRepository {
private final SessionFactory sessionFactory;
	
	public BillRepositoryImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Integer add(Bill entity) {
		if(entity == null) {
			throw new BillCannotBeNullException();
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
			throw new BillIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Bill bill = (Bill) session.get(Bill.class, id);
		
		if(bill != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(bill);
			transaction.commit();
		}
		
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
	    
	    session.close();    
	}

	public Bill get(Integer id) {
		if(id == null) {
			throw new BillIdCannotBeNullException();
		}
		
		Session session = sessionFactory.openSession();
		Bill bill = session.get(Bill.class, id);
	    session.close();    
		
		return bill;
	}

	public List<Bill> getAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM Bill b ");		
		List<Bill> authors = query.getResultList();    
		
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
	    session.close(); 
	}

}