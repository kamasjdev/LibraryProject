package interfaces;

import dto.BookCustomerDto;

public interface BookCustomerService extends BaseService<BookCustomerDto> {
	BookCustomerDto getBookCustomerByBookIdAndCustomerId(Integer id, Integer customerId);	
}