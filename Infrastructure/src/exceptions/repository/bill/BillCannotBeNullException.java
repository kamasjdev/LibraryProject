package exceptions.repository.bill;

import exceptions.InfrastructureException;

public class BillCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = -8685531013429965376L;

	public BillCannotBeNullException() {
		super("Bill cannot be null");
	}
	
	@Override
	public String getCode() {
		String code = "bill_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BillRepository";
		return className;
	}
}
