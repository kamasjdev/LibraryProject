package exceptions.repository.bill;

import exceptions.InfrastructureException;

public class BillIdCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = -2569830602178080592L;

	public BillIdCannotBeNullException() {
		super("Bill id cannot be null");
	}

	@Override
	public String getCode() {
		String code = "bill_id_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BillRepository";
		return className;
	}
}
