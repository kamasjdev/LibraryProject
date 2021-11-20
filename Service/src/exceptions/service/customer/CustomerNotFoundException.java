package exceptions.service.customer;

import exceptions.ServiceException;

public class CustomerNotFoundException extends ServiceException {
	private static final long serialVersionUID = -8308144384324200764L;
	
	public Integer customerId;
	
	@Override
	public String getCode() {
		String code = "customer_not_found";
		return code;
	}

	public CustomerNotFoundException(Integer customerId) {
		super(String.format("Customer with id: '%1$s' not found", customerId));
		this.customerId = customerId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "CustomerService";
		return clazzName;
	}
}
