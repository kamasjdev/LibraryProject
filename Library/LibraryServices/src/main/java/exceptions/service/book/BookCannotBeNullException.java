package exceptions.service.book;

import exceptions.ServiceException;

public class BookCannotBeNullException extends ServiceException  {
	private static final long serialVersionUID = -2404759883712964412L;

	@Override
	public String getCode() {
		String code = "book_cannot_be_null";
		return code;
	}
	
	public BookCannotBeNullException() {
		super("Book cannot be null");
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookService";
		return clazzName;
	}
}
