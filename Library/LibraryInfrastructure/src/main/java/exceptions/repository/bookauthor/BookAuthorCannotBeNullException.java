package exceptions.repository.bookauthor;

import exceptions.InfrastructureException;

public class BookAuthorCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = 2160496784712853169L;

	public BookAuthorCannotBeNullException() {
		super("Book author cannot be null");
	}
	
	@Override
	public String getCode() {
		String code = "book_author_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BookAuthorRepository";
		return className;
	}
}
