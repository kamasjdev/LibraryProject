package entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import common.BaseEntity;

@Entity
@Table(name = "bills")
public class Bill extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -4993408412413120019L;

	@Column(name = "cost")
	public BigDecimal cost;
	
	@Column(name = "customer_id")
	public Integer customerId;
	
	@ManyToOne
	@MapsId("customerId")
	@JoinColumn(name = "customer_id")
	public Customer customer;
	
	public static Bill create(BigDecimal cost, Integer customerId) {
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
