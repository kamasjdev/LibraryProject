package tests.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import entities.Bill;
import interfaces.BillRepository;
import interfaces.MapEntity;
import mappings.BillMapping;
import repository.BillRepositoryImpl;

public class BillCustomerTests extends BaseTest {
	private BillRepository billRepository;
	private MapEntity<Bill> mapper;
	
	public BillCustomerTests() {
		mapper = new BillMapping();
		billRepository = new BillRepositoryImpl(dbClient, mapper);
	}
	
	@Test
	public void given_valid_parameters_should_return_list_bill() {
		List<Bill> bills = billRepository.getAll();
		
		assertThat(bills).isNotNull();
		assertThat(bills.size()).isGreaterThan(0);
	}
	
	@Test
	public void given_valid_parameters_should_add_bill() {
		BigDecimal cost = new BigDecimal(100.25);
		Integer customerId = 3;
		Bill bill = Bill.create(cost, customerId);
		
		Integer id = billRepository.add(bill);
		
		assertThat(id).isGreaterThan(0);
	}
	
	@Test 
	public void given_valid_parameters_should_return_bill() {
		Integer billId = 1;
		
		Bill bill = billRepository.get(billId);
		
		assertThat(bill).isNotNull();
		assertThat(bill.id).isEqualTo(billId);
	}
	
	@Test
	public void given_valid_parameters_should_update_bill() {
		BigDecimal cost = new BigDecimal(99.99);
		Integer billId = 1;
		Bill bill = billRepository.get(billId);
		bill.cost = cost;
		
		billRepository.update(bill);
		Bill billUpdated = billRepository.get(billId);
		
		assertThat(billUpdated).isNotNull();
		assertThat(bill.cost.compareTo(cost)).isEqualTo(0);
	}
	
	@Test
	public void given_valid_parameters_should_delete_bill() {
		Integer billId = 1;
		
		billRepository.delete(billId);
		
		Bill bill = billRepository.get(billId);
		assertThat(bill).isNull();
	}
}
