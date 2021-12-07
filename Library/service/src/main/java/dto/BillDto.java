package dto;

import java.math.BigDecimal;

public class BillDto extends BaseDto {
	public BigDecimal cost;
	public Integer customerId;
	
	@Override
	public String toString() {
		String description = String.format("%1$s. %2$s %3$s", id, cost, customerId);
		return description;
	}
}
