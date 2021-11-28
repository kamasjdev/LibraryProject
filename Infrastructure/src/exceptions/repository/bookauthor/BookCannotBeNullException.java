package exceptions.repository.bookauthor;

import exceptions.InfrastructureException;

public class BookCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = 2160496784712853169L;

	public BookCannotBeNullException() {
		super("Book cannot be null");
	}
	
	@Override
	public String getCode() {
		String code = "book_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BookRepository";
		return className;
	}
}
