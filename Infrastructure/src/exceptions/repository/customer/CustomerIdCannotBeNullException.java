package exceptions.repository.customer;

import exceptions.InfrastructureException;

public class CustomerIdCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = 4731427149498281112L;

	public CustomerIdCannotBeNullException() {
		super("Customer cannot be null");
	}
	
	@Override
	public String getCode() {
		String code = "customer_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "CustomerRepository";
		return className;
	}
}
