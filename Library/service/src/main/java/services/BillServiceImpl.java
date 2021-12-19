package services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.BillDto;
import entities.Bill;
import exceptions.service.bill.BillCannotBeNullException;
import exceptions.service.bill.BillNotFoundException;
import exceptions.service.bill.InvalidBillCostException;
import exceptions.service.bill.InvalidBillCustomerIdException;
import helpers.services.mappings.Mapper;
import interfaces.BillService;
import repository.BillRepository;

@Service
@Transactional
public class BillServiceImpl implements BillService {
	private final BillRepository billRepository;
	
	public BillServiceImpl(BillRepository billRepository) {
		this.billRepository = billRepository;
	}
	
	@Override
	public BillDto getById(Integer id) {
		Bill bill = billRepository.get(id);
		
		if(bill == null) {
			return null;
		}
		
		BillDto dto = Mapper.mapToBillDto(bill);
		return dto;
	}

	@Override
	public List<BillDto> getAll() {
		List<Bill> bills = billRepository.getAll();
		List<BillDto> billsDto = new ArrayList<BillDto>();
		
		for(Bill bill : bills) {
			BillDto billDto = Mapper.mapToBillDto(bill);
			billsDto.add(billDto);
		}
		
		return billsDto;
	}

	@Override
	public void update(BillDto dto) {
		validateBill(dto);
		
		BillDto billDto = getById(dto.id);
		
		if(billDto == null) {
			throw new BillNotFoundException(dto.id);
		}
		
		billDto.cost = dto.cost;
		billDto.customerId = dto.customerId;
		Bill bill = Mapper.mapToBill(billDto);
		
		billRepository.update(bill);
	}

	@Override
	public Integer add(BillDto dto) {
		validateBill(dto);
		Bill bill = Bill.create(dto.cost, dto.customerId);
		
		Integer id = billRepository.add(bill); 
		
		return id;
	}
	
	@Override
	public void delete(Integer id) {
		BillDto bill = getById(id);
		
		if(bill == null) {
			throw new BillNotFoundException(id);
		}
		
		billRepository.delete(id);
	}
	
	private void validateBill(BillDto billDto) {
		if(billDto == null) {
			throw new BillCannotBeNullException();
		}
		
		if(billDto.customerId == null){
			throw new InvalidBillCustomerIdException(billDto.id, billDto.customerId);
		}
		
		if(billDto.customerId < 1) {
			throw new InvalidBillCustomerIdException(billDto.id, billDto.customerId);
		}
		
		if(billDto.cost == null) {
			throw new InvalidBillCostException(billDto.id, billDto.cost);
		}
		
		if(billDto.cost.compareTo(BigDecimal.ZERO) == -1) { // First BigDecimal is less than Second BigDecimal
			throw new InvalidBillCostException(billDto.id, billDto.cost);
		}
	}

	public void deleteAllBillsByCustomerId(Integer customerId) {
		billRepository.deleteAllBillsByCustomerId(customerId);		
	}
}