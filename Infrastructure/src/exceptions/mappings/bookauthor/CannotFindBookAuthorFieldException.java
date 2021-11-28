package exceptions.mappings.bookauthor;

import exceptions.InfrastructureException;

public class CannotFindBookAuthorFieldException extends InfrastructureException {
	private static final long serialVersionUID = 2095720887804022773L;
	
	public final String field;
	
	public CannotFindBookAuthorFieldException(String field) {
		super(String.format("Cannot find author field %1$s", field));
		this.field = field;
	}
	
	@Override
	public String getCode() {
		String code = "cannot_find_book_author_field";
		return code;
	}

	@Override
	public String classNameThrown() {
		String className = "BookAuthorMapping";
		return className;
	}
}
