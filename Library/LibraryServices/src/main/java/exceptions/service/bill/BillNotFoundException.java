package exceptions.service.bill;

import exceptions.ServiceException;

public class BillNotFoundException extends ServiceException {
	private static final long serialVersionUID = -8016559908520952195L;
	public final Integer billId;
	
	@Override
	public String getCode() {
		String code = "bill_not_found";
		return code;
	}

	public BillNotFoundException(Integer billId) {
		super(String.format("Author with id: '%1$s' not found", billId));
		this.billId = billId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BillService";
		return clazzName;
	}
}
