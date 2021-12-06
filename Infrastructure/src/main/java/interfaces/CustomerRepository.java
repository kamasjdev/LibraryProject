package interfaces;

import entities.Customer;

public interface CustomerRepository extends Repository<Customer> {
	int getCount();
	Customer getDetails(Integer customerId);
}
