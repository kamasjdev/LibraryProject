package exceptions.repository.bill;

import exceptions.InfrastructureException;

public class CustomerIdForBillCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = 8120547482348798052L;

	public CustomerIdForBillCannotBeNullException() {
		super("Customer id for bill cannot be null");
	}
	
	@Override
	public String getCode() {
		String code = "customer_id_for_bill_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BillRepository";
		return className;
	}
}
