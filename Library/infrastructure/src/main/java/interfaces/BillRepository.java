package interfaces;

import entities.Bill;

public interface BillRepository extends Repository<Bill> {
	void deleteAllBillsByCustomerId(Integer customerId);
	int getCount();
}
