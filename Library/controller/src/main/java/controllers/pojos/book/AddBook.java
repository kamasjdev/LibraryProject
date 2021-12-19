package controllers.pojos.book;

import java.math.BigDecimal;
import java.util.Set;

public class AddBook {
	public String bookName;
	public String ISBN;
	public BigDecimal bookCost;
	
	public Set<Integer> authorsIds;
}
