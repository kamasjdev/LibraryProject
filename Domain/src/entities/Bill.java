package entities;

import java.math.BigDecimal;

import common.BaseEntity;

public class Bill extends BaseEntity {
	private BigDecimal cost;
	private Integer customerId;
	
	public static Bill create(BigDecimal cost, Integer customerId) {
		Bill bill = new Bill();
		bill.setCost(cost);
		bill.setCustomerId(customerId);
		
		return bill;
	}
	
	@Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s", id, getCost(), getCustomerId());
		return description;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
}
