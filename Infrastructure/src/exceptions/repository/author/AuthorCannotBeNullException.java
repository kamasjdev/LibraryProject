package exceptions.repository.author;

import exceptions.InfrastructureException;

public class AuthorCannotBeNullException extends InfrastructureException {
	private static final long serialVersionUID = -5680982168307993311L;
	
	public AuthorCannotBeNullException() {
		super("Author cannot be null");
	}
	
	@Override
	public String getCode() {
		String code = "author_cannot_be_null";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "AuthorRepository";
		return className;
	}
	
	
}
