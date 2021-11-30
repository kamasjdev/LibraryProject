package exceptions.repository.customer;

import exceptions.InfrastructureException;

public class CustomerCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = 3674881007041709894L;

	public CustomerCannotBeNullException() {
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
