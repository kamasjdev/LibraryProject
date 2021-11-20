package exceptions.service.customer;

import exceptions.ServiceException;

public class InvalidCustomerLimitException extends ServiceException {
	private static final long serialVersionUID = -3994466493054296075L;
	
	public Integer customerId;
	public Integer limit;
	
	@Override
	public String getCode() {
		String code = "invalid_customer_limit";
		return code;
	}

	public InvalidCustomerLimitException(Integer customerId, Integer limit) {
		super(String.format("Invalid limit '%1$s' for customer with id: '%2$s' not found", limit, customerId));
		this.limit = limit;
		this.customerId = customerId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "CustomerService";
		return clazzName;
	}
}
