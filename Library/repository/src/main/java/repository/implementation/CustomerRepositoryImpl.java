package repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entities.Customer;
import repository.CustomerRepository;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRepositoryImpl.class);
	private final EntityManager entityManager;
	
	@Autowired
	public CustomerRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
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
		List<Customer> customers = entityManager.createQuery("from Author").getResultList();
		return customers;
	}

	@Override
	public Customer getDetails(int customerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
