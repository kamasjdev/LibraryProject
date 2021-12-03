package interfaces;

import entities.BookCustomer;

public interface BookCustomerRepository extends Repository<BookCustomer> {

	BookCustomer getBookCustomerByBookIdAndCustomerId(Integer bookId, Integer customerId);

}
