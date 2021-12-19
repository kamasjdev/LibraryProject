package exceptions.service.book;

import exceptions.ServiceException;

public class CustomerCannotBorrowBookException extends ServiceException {
	private static final long serialVersionUID = -6152749190781481935L;
	
	public final Integer cutomerId;
	
	public CustomerCannotBorrowBookException(Integer customerId) {
		super(String.format("Customer with id: %1$s cant borrow any book", customerId));
		this.cutomerId = customerId;
	}
	
	@Override
	public String getCode() {
		String code = "customer_cannot_borrow_book";
		return code;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookService";
		return clazzName;
	}
}
