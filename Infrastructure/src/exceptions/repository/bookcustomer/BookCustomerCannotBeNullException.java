package exceptions.repository.bookcustomer;

import exceptions.InfrastructureException;

public class BookCustomerCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = 8730067146954905567L;

	public BookCustomerCannotBeNullException() {
		super("Book customer cannot be null");
	}
	
	@Override
	public String getCode() {
		String code = "book_customer_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BookCustomerRepository";
		return className;
	}
}
