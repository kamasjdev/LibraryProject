package exceptions.service.customer;

import exceptions.ServiceException;

public class CustomerCannotBeNullException extends ServiceException {
	private static final long serialVersionUID = 4345626297462105981L;

	@Override
	public String getCode() {
		String code = "customer_cannot_be_null";
		return code;
	}
	
	public CustomerCannotBeNullException() {
		super("Customer cannot be null");
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "CustomerService";
		return clazzName;
	}
}
