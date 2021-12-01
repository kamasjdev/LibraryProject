package exceptions.repository.bookauthor;

import exceptions.InfrastructureException;

public class BookAuthorsCannotBeEmptyOrNullException  extends InfrastructureException {
	private static final long serialVersionUID = -9031497292198868356L;

	public BookAuthorsCannotBeEmptyOrNullException() {
		super("Authors cannot be null or empty");
	}
	
	@Override
	public String getCode() {
		String code = "authors_cannot_be_empty_or_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BookRepository";
		return className;
	}
}
