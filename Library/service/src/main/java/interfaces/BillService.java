package interfaces;

import dto.BillDto;

public interface BillService extends BaseService<BillDto> {
	void deleteAllBillsByCustomerId(Integer customerId);
}