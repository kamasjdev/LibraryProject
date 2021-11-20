package exceptions.service.bookauthor;

import exceptions.ServiceException;

public class BookAuthorCannotBeNullException extends ServiceException {
	private static final long serialVersionUID = 4604627802151486416L;
	
	@Override
	public String getCode() {
		String code = "book_author_cannot_be_null";
		return code;
	}
	
	public BookAuthorCannotBeNullException() {
		super("Book Author cannot be null");
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookAuthorService";
		return clazzName;
	}
}
