package repository;

import entities.Bill;

public interface BillRepository extends Repository<Bill> {
	void deleteAllBillsByCustomerId(int customerId);
}
