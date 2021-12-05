package exceptions.repository.author;

import exceptions.InfrastructureException;

public class AuthorIdCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = -5347172409313252078L;
	
	public AuthorIdCannotBeNullException() {
		super("Author id cannot be null");
	}

	@Override
	public String getCode() {
		String code = "author_id_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "AuthorRepository";
		return className;
	}
}
