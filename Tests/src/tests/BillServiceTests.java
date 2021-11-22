package tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import entities.Bill;
import exceptions.service.bill.BillNotFoundException;
import exceptions.service.bill.InvalidBillCostException;
import exceptions.service.bill.InvalidBillCustomerIdException;
import services.BillService;

public class BillServiceTests {
	private BillService billService;
	
	public BillServiceTests() {
		billService = new BillService();
	}
	
	@Test
	public void given_valid_parameters_should_add_bill() {
		BigDecimal cost = new BigDecimal(100.25);
		Integer customerId = 1;
		Integer expectedId = 1;
		Bill bill = Bill.create(cost, customerId);
		
		Integer id = billService.add(bill);
		
		assertThat(id).isEqualTo(expectedId);
	}
	
	@Test
	public void given_invalid_cost_when_add_should_throw_an_exception() {
		Integer customerId = 1;
		Bill bill = Bill.create(null, customerId);
		InvalidBillCostException expectedException = new InvalidBillCostException(bill.id, null);
		
		InvalidBillCostException thrown = (InvalidBillCostException) catchThrowable(() -> billService.add(bill));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.billId).isNull();
		assertThat(thrown.cost).isNull();
	}
	
	@Test
	public void given_invalid_customer_id_when_add_should_throw_an_exception() {
		BigDecimal cost = new BigDecimal(100.25);
		Integer customerId = null;
		Bill bill = Bill.create(cost, customerId);
		InvalidBillCustomerIdException expectedException = new InvalidBillCustomerIdException(bill.id, bill.customerId);
		
		InvalidBillCustomerIdException thrown = (InvalidBillCustomerIdException) catchThrowable(() -> billService.add(bill));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.billId).isNull();
		assertThat(thrown.customerId).isNull();
	}
	
	@Test
	public void given_valid_parameters_should_update_bill() {
		BigDecimal cost = new BigDecimal(100.25);
		Integer customerId = 1;
		Integer customerIdAfterChange = 2;
		Bill bill = Bill.create(cost, customerId);
		Integer id = billService.add(bill);
		
		Bill billAdded = billService.getById(id);
		billAdded.customerId = customerIdAfterChange;
		billService.update(billAdded);
		Bill billToCheck = billService.getById(id);
		
		assertThat(billToCheck.customerId).isEqualTo(customerIdAfterChange);
	}
	
	@Test
	public void given_invalid_cost_when_update_should_throw_an_exception() {
		Integer customerId = 1;
		BigDecimal cost = new BigDecimal(100.21);
		Bill bill = Bill.create(cost, customerId);
		Integer id = billService.add(bill);
		BigDecimal costAfterUpdate = new BigDecimal(-124);
		InvalidBillCostException expectedException = new InvalidBillCostException(bill.id, costAfterUpdate);
		
		Bill billFromService = billService.getById(id);
		billFromService.cost = costAfterUpdate;
		InvalidBillCostException thrown = (InvalidBillCostException) catchThrowable(() -> billService.update(billFromService));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.billId).isEqualTo(id);
		assertThat(thrown.cost).isEqualTo(costAfterUpdate);
	}
	
	@Test
	public void given_invalid_customer_id_when_update_should_throw_an_exception() {
		Integer customerId = 1;
		BigDecimal cost = new BigDecimal(100.21);
		Bill bill = Bill.create(cost, customerId);
		Integer id = billService.add(bill);
		Integer customerAfterUpdateId = 0;
		InvalidBillCustomerIdException expectedException = new InvalidBillCustomerIdException(bill.id, customerAfterUpdateId);
		
		Bill billFromService = billService.getById(id);
		billFromService.customerId = customerAfterUpdateId;
		InvalidBillCustomerIdException thrown = (InvalidBillCustomerIdException) catchThrowable(() -> billService.update(billFromService));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.billId).isEqualTo(id);
		assertThat(thrown.customerId).isEqualTo(customerAfterUpdateId);
	}	
	
	@Test
	public void given_valid_parameters_should_delete_bill() {
		Integer customerId = 1;
		BigDecimal cost = BigDecimal.ONE;
		Integer id = billService.add(Bill.create(cost, customerId));
		int expectedSize = 0;
		
		billService.delete(id);
		List<Bill> bills = billService.getEntities();
		
		assertThat(bills.size()).isEqualTo(expectedSize);
	}
	
	@Test
	public void given_invalid_id_when_delete_should_thrown_an_exception() {
		Integer id = 1;
		BillNotFoundException expectedException = new BillNotFoundException(id);
		
		BillNotFoundException thrown = (BillNotFoundException) catchThrowable(() -> billService.delete(id));
		
		assertThat(thrown).isInstanceOf(expectedException.getClass());
		assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		assertThat(thrown.billId).isEqualTo(id);
	}
}
