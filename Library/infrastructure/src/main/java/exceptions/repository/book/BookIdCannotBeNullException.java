package exceptions.repository.book;

import exceptions.InfrastructureException;

public class BookIdCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = -8416248287998521153L;

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
