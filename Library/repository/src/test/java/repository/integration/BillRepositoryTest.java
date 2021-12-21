package repository.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import entities.Bill;
import repository.BillRepository;
import repository.configuration.PersistenceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class )
@TestPropertySource(locations="classpath:persistence-h2.properties")
@Transactional
@Sql({"classpath:init.sql"})
public class BillRepositoryTest {
	
	@Autowired
	private BillRepository billRepository;
	
	@Test
	public void given_valid_id_should_return_bill_from_db() {
		Integer id = 2;
		
		Bill bill = billRepository.get(id);
		
		assertThat(bill).isNotNull();
		assertThat(bill.id).isEqualTo(id);
	}
	
	@Test
	public void given_bill_should_add_to_db() {
		BigDecimal bookCost = new BigDecimal(100);
		Integer customerId = 1;
		Bill bill = Bill.create(bookCost, customerId);
		
		Integer id = billRepository.add(bill);
		
		Bill billFromDb = billRepository.get(id);
		assertThat(billFromDb).isNotNull();
		assertThat(billFromDb.id).isEqualTo(id);
	}
	
	@Test
	public void given_bills_from_init_should_return_more_than_one_bill() {
		int size = 4;
		
		List<Bill> bills = billRepository.getAll();
		
		assertThat(bills.size()).isGreaterThan(0);
		assertThat(bills.size()).isEqualTo(size);
	}
	
	@Test
	public void given_valid_bill_should_delete_from_db() {
		BigDecimal bookCost = new BigDecimal(100);
		Integer customerId = 1;
		Bill bill = Bill.create(bookCost, customerId);	
		Integer id = billRepository.add(bill);
		
		billRepository.delete(id);
		
		Bill billFromDb = billRepository.get(id);
		assertThat(billFromDb).isNull();
	}
	
	@Test
	public void given_valid_bill_should_update() {
		Integer id = 1;
		BigDecimal bookCost = new BigDecimal(100);
		Bill bill = billRepository.get(id);
		bill.cost = bookCost;
		
		billRepository.update(bill);
		
		Bill billUpdated = billRepository.get(id);
		assertThat(billUpdated.cost).isEqualTo(bookCost);
	}
}
