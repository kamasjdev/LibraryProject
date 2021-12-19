package controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.BillDto;
import interfaces.BillService;

@RestController
@RequestMapping("/bills")
public class BillController {
	
	private static final Logger logger = LoggerFactory.getLogger(BillController.class);
	private final BillService billService;
	
	@Autowired
	public BillController(BillService billService) {
		this.billService = billService;
	}
	
	@GetMapping("{billId}")
	public BillDto getBill(@PathVariable Integer billId) {
		logger.info(String.format("Getting bill by id: %1$s", billId));
		BillDto billDto = billService.getById(billId);
		return billDto;
	}
	
	@GetMapping
	public List<BillDto> getAll() {
		logger.info("Getting bills");
		List<BillDto> bills = billService.getAll();
		return bills;
	}
	
	@DeleteMapping("{billId}")
	public void deleteBill(@PathVariable int billId) {
		logger.info(String.format("Deleting bill lwith id: %1$s", billId));
		billService.delete(billId);
	}
	
	@DeleteMapping("by-customer/{customerId}")
	public void deleteCustomerBills(@PathVariable int customerId) {
		logger.info(String.format("Deleting all customer bills with id: %1$s", customerId));
		billService.deleteAllBillsByCustomerId(customerId);
	}
}
