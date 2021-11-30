package tests.integration;

import entities.Bill;
import interfaces.BillRepository;
import interfaces.MapEntity;
import mappings.BillMapping;
import repository.BillRepositoryImpl;

public class BillCustomerTests extends BaseTest {
	private BillRepository customerRepository;
	private MapEntity<Bill> mapper;
	
	public BillCustomerTests() {
		mapper = new BillMapping();
		customerRepository = new BillRepositoryImpl(dbClient, mapper);
	}
}
