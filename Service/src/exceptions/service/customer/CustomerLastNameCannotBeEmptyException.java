package exceptions.service.customer;

import exceptions.ServiceException;

public class CustomerLastNameCannotBeEmptyException extends ServiceException {
	private static final long serialVersionUID = -7304428357591804797L;
	
	public Integer customerId;

	@Override
	public String getCode() {
		String code = "customer_last_name_cannot_be_empty";
		return code;
	}

	public CustomerLastNameCannotBeEmptyException(Integer customerId) {
		super(String.format("Customer last name with id: '%1$s' cannot be empty", customerId));
		this.customerId = customerId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "CustomerService";
		return clazzName;
	}
}
