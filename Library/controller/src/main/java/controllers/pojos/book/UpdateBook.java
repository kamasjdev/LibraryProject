package controllers.pojos.book;

import java.math.BigDecimal;
import java.util.Set;

public class UpdateBook {
	public Integer id;
	public String bookName;
	public String ISBN;
	public BigDecimal bookCost;
	
	public Set<Integer> authorsIds;
}
