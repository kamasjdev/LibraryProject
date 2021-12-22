package service.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.BillDto;
import entities.Bill;
import exceptions.service.bill.BillNotFoundException;
import exceptions.service.bill.InvalidBillCostException;
import exceptions.service.bill.InvalidBillCustomerIdException;
import interfaces.BillService;
import repository.BillRepository;
import services.BillServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BillServiceTest {

	 private BillService billService;
	 private BillRepository billRepository;
	
	 @Before 
	 public void init() {
		 billRepository = Mockito.mock(BillRepository.class);
		 billService = new BillServiceImpl(billRepository);
     }
	 
	 @Test
	 public void given_valid_parameters_should_add_bill() {
	 	 BillDto bill = createBill();
		
		 billService.add(bill);
		
		 verify(billRepository).add(any(Bill.class));
	 }
	
	 @Test
	 public void given_invalid_cost_when_add_should_throw_an_exception() {
		 BillDto bill = createBill();
		 bill.cost = null;
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
		 BillDto bill = createBill(cost, customerId);
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
		 BillDto billDto = createBill(cost, customerIdAfterChange);
		 Integer id = 1;
		 billDto.id = id;
		 Bill bill = Bill.create(cost, customerId);
		 bill.id = id;
		 doReturn(bill).when(billRepository).get(any(Integer.class));
		
		 billService.update(billDto);
		
		 verify(billRepository, times(1)).update(any(Bill.class));
	 }
	
	 @Test
	 public void given_invalid_cost_when_update_should_throw_an_exception() {
		 Integer customerId = 1;
		 BigDecimal cost = new BigDecimal(-100.21);
		 Integer id = 1;
		 BillDto billDto = createBill(cost, customerId);
		 billDto.id = id;
		 Bill bill = Bill.create(cost, customerId);
		 bill.id = id;
		 doReturn(bill).when(billRepository).get(any(Integer.class));
		 InvalidBillCostException expectedException = new InvalidBillCostException(bill.id, cost);
		
		 InvalidBillCostException thrown = (InvalidBillCostException) catchThrowable(() -> billService.update(billDto));
	 	
	 	 assertThat(thrown).isInstanceOf(expectedException.getClass());
		 assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		 assertThat(thrown.billId).isEqualTo(id);
		 assertThat(thrown.cost).isEqualTo(cost);
	 }
	
	 @Test
	 public void given_invalid_customer_id_when_update_should_throw_an_exception() {
		 Integer id = 1;
		 Integer customerId = 1;
		 BigDecimal cost = new BigDecimal(100.21);
		 Bill bill = Bill.create(cost, customerId);
		 bill.id = id;
		 Integer customerAfterUpdateId = 0;
		 BillDto billDto = createBill(cost, customerAfterUpdateId);
		 billDto.id = id;
		 doReturn(bill).when(billRepository).get(any(Integer.class));
		 InvalidBillCustomerIdException expectedException = new InvalidBillCustomerIdException(bill.id, customerAfterUpdateId);
		
		 InvalidBillCustomerIdException thrown = (InvalidBillCustomerIdException) catchThrowable(() -> billService.update(billDto));
		
		 assertThat(thrown).isInstanceOf(expectedException.getClass());
		 assertThat(thrown.getMessage()).isEqualTo(expectedException.getMessage());
		 assertThat(thrown.billId).isEqualTo(id);
		 assertThat(thrown.customerId).isEqualTo(customerAfterUpdateId);
 	 } 	
	
	 @Test
	 public void given_valid_parameters_should_delete_bill() {
		 Integer customerId = 1;
		 BigDecimal cost = BigDecimal.ONE;
		 Integer id = billService.add(createBill(cost, customerId));
		 doReturn(Bill.create(cost, customerId)).when(billRepository).get(any(Integer.class));
	 	
		 billService.delete(id);
		
		 verify(billRepository, times(1)).delete(any(Integer.class));
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
		
	 private BillDto createBill() {
		 BillDto bill = new BillDto();
		 bill.cost = new BigDecimal(100);
		 bill.customerId = 1;
		
		 return bill;
	 }
	
	 private BillDto createBill(BigDecimal cost, Integer customerId) {
		 BillDto bill = new BillDto();
		 bill.cost = cost;
		 bill.customerId = customerId;
		
		 return bill;
	 }
}
