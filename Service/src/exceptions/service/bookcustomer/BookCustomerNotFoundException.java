package exceptions.service.bookcustomer;

import exceptions.ServiceException;

public class BookCustomerNotFoundException extends ServiceException {
	private static final long serialVersionUID = 3328513799558236465L;
	
	public Integer bookCustomerId;
	
	@Override
	public String getCode() {
		String code = "book_customer_not_found";
		return code;
	}

	public BookCustomerNotFoundException(Integer bookCustomerId) {
		super(String.format("Book Customer with id: '%1$s' not found", bookCustomerId));
		this.bookCustomerId = bookCustomerId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookCustomerService";
		return clazzName;
	}
}
