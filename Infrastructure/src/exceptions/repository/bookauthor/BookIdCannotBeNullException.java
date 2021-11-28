package exceptions.repository.bookauthor;

import exceptions.InfrastructureException;

public class BookIdCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = -2025330277613976473L;

	public BookIdCannotBeNullException() {
		super("Book id cannot be null");
	}

	@Override
	public String getCode() {
		String code = "book_id_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BookRepository";
		return className;
	}
}
