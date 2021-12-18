package repository;

import entities.Customer;

public interface CustomerRepository extends Repository<Customer> {
	Customer getDetails(int customerId);
}
