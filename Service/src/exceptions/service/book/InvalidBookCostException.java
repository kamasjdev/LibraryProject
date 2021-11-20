package exceptions.service.book;

import java.math.BigDecimal;

import exceptions.ServiceException;

public class InvalidBookCostException extends ServiceException {
	private static final long serialVersionUID = -4117086712580661889L;
	
	public Integer bookId;
	public BigDecimal cost;
	
	@Override
	public String getCode() {
		String code = "invalid_book_cost";
		return code;
	}

	public InvalidBookCostException(Integer bookId, BigDecimal cost) {
		super(String.format("Invalid cost '%1$s' for book with id: '%2$s'", cost, bookId));
		this.bookId = bookId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookService";
		return clazzName;
	}
}
