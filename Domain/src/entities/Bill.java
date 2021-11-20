package entities;

import java.math.BigDecimal;

import common.BaseEntity;

public class Bill extends BaseEntity {
	public BigDecimal cost;
	public Integer customerId;
	
	public static Bill Create(BigDecimal cost, Integer customerId) {
		Bill bill = new Bill();
		bill.cost = cost;
		bill.customerId = customerId;
		
		return bill;
	}
	
	@Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s", id, cost, customerId);
		return description;
	}
}
