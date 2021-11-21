package services;

import java.math.BigDecimal;
import java.util.List;

import abstracts.AbstractBaseService;
import entities.Bill;
import exceptions.service.bill.BillCannotBeNullException;
import exceptions.service.bill.BillNotFoundException;
import exceptions.service.bill.InvalidBillCostException;
import exceptions.service.bill.InvalidBillCustomerIdException;

public class BillService extends AbstractBaseService<Bill> {
		
	@Override
	public Bill getById(Integer id) {
		Bill bill = objects.stream().filter(o->o.id.equals(id)).findFirst().orElse(null);
		return bill;
	}

	@Override
	public List<Bill> getEntities() {
		return objects;
	}

	@Override
	public void update(Bill entity) {
		validateBill(entity);
		
		Bill bill = getById(entity.id);
		
		if(bill == null) {
			throw new BillNotFoundException(entity.id);
		}
		
		bill.cost = entity.cost;
		bill.customerId = entity.customerId;
	}

	@Override
	public Integer add(Bill entity) {
		validateBill(entity);
		
		Integer id = getLastId();
		entity.id = id;
		objects.add(entity);
		
		return id;
	}
	
	@Override
	public void delete(Integer id) {
		Bill bill = getById(id);
		
		if(bill == null) {
			throw new BillNotFoundException(id);
		}
		
		objects.remove(bill);
	}
	
	private void validateBill(Bill bill) {
		if(bill == null) {
			throw new BillCannotBeNullException();
		}
		
		if(bill.customerId == null){
			throw new InvalidBillCustomerIdException(bill.id, bill.customerId);
		}
		
		if(bill.customerId < 1) {
			throw new InvalidBillCustomerIdException(bill.id, bill.customerId);
		}
		
		if(bill.cost.compareTo(BigDecimal.ZERO) == -1) { // First BigDecimal is less than Second BigDecimal
			throw new InvalidBillCostException(bill.id, bill.cost);
		}
	}
}
