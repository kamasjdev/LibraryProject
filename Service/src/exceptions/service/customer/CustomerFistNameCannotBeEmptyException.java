
package exceptions.service.customer;

import exceptions.ServiceException;

public class CustomerFistNameCannotBeEmptyException extends ServiceException {
	private static final long serialVersionUID = -8468852028339114772L;
	
	public Integer customerId;
	
	@Override
	public String getCode() {
		String code = "customer_first_name_cannot_be_empty";
		return code;
	}

	public CustomerFistNameCannotBeEmptyException(Integer customerId) {
		super(String.format("Customer first name with id: '%1$s' cannot be empty", customerId));
		this.customerId = customerId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "CustomerService";
		return clazzName;
	}
}
