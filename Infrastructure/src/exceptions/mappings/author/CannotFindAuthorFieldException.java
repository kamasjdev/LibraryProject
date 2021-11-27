package exceptions.mappings.author;

import exceptions.InfrastructureException;

public class CannotFindAuthorFieldException extends InfrastructureException {
	private static final long serialVersionUID = 7164626352335768950L;

	public CannotFindAuthorFieldException() {
		super("Cannot find author field");
	}
	
	@Override
	public String getCode() {
		String code = "cannot_find_author_field";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "AuthorMapping";
		return className;
	}

}
