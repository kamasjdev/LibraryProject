package exceptions.repository.book;

import exceptions.InfrastructureException;

public class BookCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = 7422959938993889287L;

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
