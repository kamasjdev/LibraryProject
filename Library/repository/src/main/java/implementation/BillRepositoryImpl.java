package implementation;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entities.Bill;
import repository.BillRepository;

@Repository
public class BillRepositoryImpl implements BillRepository {

	private static final Logger logger = LoggerFactory.getLogger(BillRepositoryImpl.class);
	private final EntityManager entityManager;
	
	@Autowired
	public BillRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Integer add(Bill bill) {
		logger.info(String.format("Adding entity %1$s", Bill.class.getName()));
        entityManager.persist(bill);
		return bill.id;
	}

	@Override
	public void delete(Integer id) {
		logger.info(String.format("Deleting entity %1$s", Bill.class.getName()));
		final Bill bill = get(id);
        delete(bill);
	}

	@Override
	public void delete(Bill bill) {
		logger.info(String.format("Deleting entity %1$s", Bill.class.getName()));
        entityManager.remove(bill);		
	}

	@Override
	public void update(Bill bill) {
		logger.info(String.format("Updating entity %1$s", Bill.class.getName()));
        entityManager.merge(bill);
	}

	@Override
	public Bill get(Integer id) {
		logger.info(String.format("Getting entity %1$s", Bill.class.getName()));
		Bill bill = entityManager.find(Bill.class, id);
		return bill;
	}

	@Override
	public List<Bill> getAll() {
		logger.info(String.format("Getting all entities %1$s", Bill.class.getName()));
		List<Bill> bills = entityManager.createQuery("from Author").getResultList();
		return bills;
	}

}
