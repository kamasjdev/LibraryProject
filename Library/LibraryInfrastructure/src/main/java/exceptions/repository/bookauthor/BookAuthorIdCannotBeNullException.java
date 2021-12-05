package exceptions.repository.bookauthor;

import exceptions.InfrastructureException;

public class BookAuthorIdCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = -2025330277613976473L;

	public BookAuthorIdCannotBeNullException() {
		super("Book author id cannot be null");
	}

	@Override
	public String getCode() {
		String code = "book_author_id_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BookAuthorRepository";
		return className;
	}
}
