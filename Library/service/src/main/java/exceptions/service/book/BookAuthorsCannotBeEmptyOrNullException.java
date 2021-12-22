package exceptions.service.book;

import exceptions.ServiceException;

public class BookAuthorsCannotBeEmptyOrNullException extends ServiceException  {

	private static final long serialVersionUID = 1001616572846840828L;

	@Override
	public String getCode() {
		String code = "book_authors_cannot_be_empty_or_null";
		return code;
	}
	
	public BookAuthorsCannotBeEmptyOrNullException() {
		super("Book cannot have empty or null authors");
	}
	
	@Override
	public String classNameThrown() {
		String clazzName = "BookService";
		return clazzName;
	}
}
