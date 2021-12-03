package services;

import java.math.BigDecimal;
import java.util.List;

import entities.Bill;
import exceptions.service.bill.BillCannotBeNullException;
import exceptions.service.bill.BillNotFoundException;
import exceptions.service.bill.InvalidBillCostException;
import exceptions.service.bill.InvalidBillCustomerIdException;
import interfaces.BaseService;
import interfaces.BillRepository;

public class BillService implements BaseService<Bill> {
	private final BillRepository billRepository;
	
	public BillService(BillRepository billRepository) {
		this.billRepository = billRepository;
	}
	
	@Override
	public Bill getById(Integer id) {
		Bill bill = billRepository.get(id);
		return bill;
	}

	@Override
	public List<Bill> getEntities() {
		List<Bill> bills = billRepository.getAll();
		return bills;
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
		
		billRepository.update(bill);
	}

	@Override
	public Integer add(Bill entity) {
		validateBill(entity);
		
		Integer id = billRepository.add(entity); 
		
		return id;
	}
	
	@Override
	public void delete(Integer id) {
		Bill bill = getById(id);
		
		if(bill == null) {
			throw new BillNotFoundException(id);
		}
		
		billRepository.delete(bill);
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
		
		if(bill.cost == null) {
			throw new InvalidBillCostException(bill.id, bill.cost);
		}
		
		if(bill.cost.compareTo(BigDecimal.ZERO) == -1) { // First BigDecimal is less than Second BigDecimal
			throw new InvalidBillCostException(bill.id, bill.cost);
		}
	}
}
