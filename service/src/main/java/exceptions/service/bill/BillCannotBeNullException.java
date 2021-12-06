package exceptions.service.bill;

import exceptions.ServiceException;

public class BillCannotBeNullException extends ServiceException  {
	private static final long serialVersionUID = 3527865391877582045L;

	@Override
	public String getCode() {
		String code = "bill_cannot_be_null";
		return code;
	}
	
	public BillCannotBeNullException() {
		super("Bill cannot be null");
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BillService";
		return clazzName;
	}
}
