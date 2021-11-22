package exceptions.service.bill;

import java.math.BigDecimal;

import exceptions.ServiceException;

public class InvalidBillCostException extends ServiceException {
	private static final long serialVersionUID = 5710361634807030940L;
	
	public Integer billId;
	public BigDecimal cost;
	
	@Override
	public String getCode() {
		String code = "invalid_bill_cost";
		return code;
	}

	public InvalidBillCostException(Integer billId, BigDecimal cost) {
		super(String.format("Invalid cost '%1$s' for bill with id: '%2$s'", cost, billId));
		this.billId = billId;
		this.cost = cost;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BillService";
		return clazzName;
	}
}
