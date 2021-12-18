package repository;

import entities.BookCustomer;

public interface BookCustomerRepository extends Repository<BookCustomer> {
	BookCustomer getBookCustomerByBookIdAndCustomerId(int bookId, int customerId);
}
