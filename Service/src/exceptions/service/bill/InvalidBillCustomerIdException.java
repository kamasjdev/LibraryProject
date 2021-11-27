package exceptions.service.bill;

import exceptions.ServiceException;

public class InvalidBillCustomerIdException extends ServiceException {
	private static final long serialVersionUID = -6924581582329981176L;
	public final Integer billId;
	public final Integer customerId;
	
	@Override
	public String getCode() {
		String code = "invalid_bill_customer_id";
		return code;
	}

	public InvalidBillCustomerIdException(Integer billId, Integer customerId) {
		super(String.format("Invalid customer id: '%1$s' for bill with id: '%2$s'", customerId, billId));
		this.billId = billId;
		this.customerId = customerId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BillService";
		return clazzName;
	}
}
