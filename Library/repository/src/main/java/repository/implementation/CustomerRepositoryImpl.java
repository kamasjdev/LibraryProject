package repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entities.Customer;
import repository.CustomerRepository;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRepositoryImpl.class);
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Integer add(Customer customer) {
		logger.info(String.format("Adding entity %1$s", Customer.class.getName()));
        entityManager.persist(customer);
		return customer.id;
	}

	@Override
	public void delete(Integer id) {
		logger.info(String.format("Deleting entity %1$s", Customer.class.getName()));
		final Customer customer = get(id);
		logger.info(String.format("Got customer %1$s", customer.id));
        delete(customer);
	}

	@Override
	public void delete(Customer customer) {
		logger.info(String.format("Deleting entity %1$s", Customer.class.getName()));
        entityManager.remove(customer);		
	}

	@Override
	public void update(Customer customer) {
		logger.info(String.format("Updating entity %1$s", Customer.class.getName()));
        entityManager.merge(customer);
	}

	@Override
	public Customer get(Integer id) {
		logger.info(String.format("Getting entity %1$s", Customer.class.getName()));
		Customer customer = entityManager.find(Customer.class, id);
		return customer;
	}

	@Override
	public List<Customer> getAll() {
		logger.info(String.format("Getting all entities %1$s", Customer.class.getName()));
		List<Customer> customers = entityManager.createQuery("from Customer").getResultList();
		return customers;
	}

	@Override
	public Customer getDetails(int customerId) {
		logger.info(String.format("Getting customer details with id: %1$s", customerId));
		Query query = entityManager.createQuery(
				"SELECT c " +
				"FROM Customer as c " +
				"LEFT JOIN FETCH c.books as bc " +
				"LEFT JOIN FETCH bc.book as b " +
				"LEFT JOIN FETCH c.bills as bill " +
				"WHERE c.id = :id");
		query.setParameter("id", customerId);
		Customer customer = (Customer) query.getResultList().get(0);
		return customer;
	}

}
