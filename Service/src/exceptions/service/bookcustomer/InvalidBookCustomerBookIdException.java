package exceptions.service.bookcustomer;

import exceptions.ServiceException;

public class InvalidBookCustomerBookIdException  extends ServiceException {
	private static final long serialVersionUID = 8470861420098026734L;
	
	public Integer bookCustomerId; 
	public Integer bookId;
	
	@Override
	public String getCode() {
		String code = "invalid_book_customer_book_id";
		return code;
	}
	
	public InvalidBookCustomerBookIdException(Integer bookCustomerId, Integer bookId) {
		super(String.format("Invalid book id '%1$s' for book author with id '%2$s'", bookId, bookCustomerId));
		this.bookCustomerId = bookCustomerId;
		this.bookId = bookId;
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookAuthorService";
		return clazzName;
	}
}
