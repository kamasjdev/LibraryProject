package exceptions.service.bookcustomer;

import exceptions.ServiceException;

public class InvalidBookCustomerCustomerIdException extends ServiceException {
	private static final long serialVersionUID = 294780677919046705L;
	
	public Integer bookCustomerId;
	public Integer customerId; 
		
	@Override
	public String getCode() {
		String code = "invalid_book_customer_customer_id";
		return code;
	}
	
	public InvalidBookCustomerCustomerIdException(Integer bookCustomerId, Integer customerId) {
		super(String.format("Invalid customer id '%1$s' for book customer with id '%$2s'", bookCustomerId, customerId));
		this.bookCustomerId = bookCustomerId;
		this.customerId = customerId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookCustomerService";
		return clazzName;
	}
}
