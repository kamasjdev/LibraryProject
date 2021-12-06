package exceptions.service.bookcustomer;

import exceptions.ServiceException;

public class BookCustomerCannotBeNullException extends ServiceException {
	private static final long serialVersionUID = 2347517944652497101L;

	@Override
	public String getCode() {
		String code = "book_customer_cannot_be_null";
		return code;
	}
	
	public BookCustomerCannotBeNullException() {
		super("Book Customer cannot be null");
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookCustomerService";
		return clazzName;
	}
}
